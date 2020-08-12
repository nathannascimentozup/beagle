/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import UIKit
import YogaKit
import BeagleSchema

/// Defines a container that holds a listview item
final class ListViewCell: UICollectionViewCell {
    
    private var containerView: UIView?
    
    var item: Int?
    var context: Context? {
        get {
            return containerView?.contextMap["item"]?.value
        }
        set {
            if let context = newValue {
                containerView?.setContext(context)
            }
        }
    }
    
    func configure(item: Int, listView: ListViewUIComponent) {
        let context: Context
        if let savedContext = listView.contextResolver.context(for: item) {
            context = savedContext
        } else {
            let value = listView.model.listViewItems?[item] ?? .empty
            context = Context(id: "item", value: value)
        }
        self.item = item
        self.context = context
        
        let flexDirection = listView.model.direction.flexDirection
        let container = containerView ?? UIView()
        let template = container.subviews.first ?? listView.renderer.render(listView.model.template)
        container.removeFromSuperviewAndYogaTree()
        
        container.parentContext = listView
        container.style.setup(Style().flex(Flex()
            .flexDirection(flexDirection)
            .shrink(0)
        ))
        if template.superview != container {
            template.removeFromSuperviewAndYogaTree()
            container.addSubview(template)
        }
        container.setContext(context)

        let host = UIView()
        host.isHidden = true
        host.style.setup(Style().flex(Flex().flexDirection(flexDirection)))
        host.yoga.overflow = .scroll
        host.addSubview(container)
        host.frame = listView.collectionView.bounds
        
        (listView.renderer.controller as? BeagleScreenViewController)?.configBindings()
        
        host.style.applyLayout()
        let size = container.bounds.size
        frame.size = size
        contentView.frame.size = size
        container.removeFromSuperviewAndYogaTree()
        
        contentView.addSubview(container)
        containerView = container

        host.removeFromSuperviewAndYogaTree()
    }
    
    override func preferredLayoutAttributesFitting(
        _ layoutAttributes: UICollectionViewLayoutAttributes
    ) -> UICollectionViewLayoutAttributes {
        layoutAttributes.size = frame.size
        return layoutAttributes
    }
}

extension UIView {
    fileprivate func removeFromSuperviewAndYogaTree() {
        removeFromSuperview()
        YGNodeRemoveAllChildren(yoga.node)
    }
}
