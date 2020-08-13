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
import BeagleSchema

extension ListView.Direction {
    var scrollDirection: UICollectionView.ScrollDirection {
        switch self {
        case .vertical:
            return .vertical
        case .horizontal:
            return .horizontal
        }
    }
    var flexDirection: Flex.FlexDirection {
        switch self {
        case .vertical:
            return .column
        case .horizontal:
            return .row
        }
    }
}

extension ListView: ServerDrivenComponent {

    public func toView(renderer: BeagleRenderer) -> UIView {
        let view = ListViewUIComponent(
            model: ListViewUIComponent.Model(
                listViewItems: nil,
                direction: direction ?? .vertical,
                template: template,
                onScrollEnd: onScrollEnd,
                scrollThreshold: scrollThreshold,
                useParentScroll: useParentScroll
            ),
            renderer: renderer
        )
        
        renderer.controller.execute(actions: onInit, origin: view)
        renderer.observe(dataSource, andUpdateManyIn: view) {
            view.listViewItems = $0
        }
        view.style.setup(widgetProperties.style)
        return view
    }
}
