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

struct ListItemContextResolver {
    
    private var orphanCells = [Int: ListViewCell]()
    private var contexts = [Int: Context]()
    
    mutating func track(orphanCell cell: ListViewCell) {
        if let item = cell.item {
            orphanCells[item] = cell
        }
    }
    
    mutating func reuse(cell: ListViewCell) {
        guard let item = cell.item else { return }
        contexts[item] = cell.context
        orphanCells.removeValue(forKey: item)
    }
        
    mutating func context(for item: Int) -> Context? {
        if let orphan = orphanCells[item] {
            reuse(cell: orphan)
        }
        return contexts[item]
    }
    
    mutating func reset() {
        while let (_, cell) = orphanCells.popFirst() {
            cell.item = nil
        }
        contexts.removeAll()
    }
    
}

final class ListViewUIComponent: UIView {
    
    // MARK: - Properties
    
    var contextResolver = ListItemContextResolver()
    var renderer: BeagleRenderer
    var model: Model
    var validationSetOnScrollEnd = true
    
    var listViewItems: [DynamicObject]? {
        get { model.listViewItems }
        set {
            model.listViewItems = newValue
            contextResolver.reset()
            collectionView.reloadData()
        }
    }
    
    // MARK: - UIComponents
    
    lazy var collectionViewFlowLayout: UICollectionViewFlowLayout = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = model.direction.scrollDirection
        layout.minimumInteritemSpacing = 0
        layout.minimumLineSpacing = 0
        layout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        layout.itemSize = UICollectionViewFlowLayout.automaticSize
        return layout
    }()
    
    lazy var collectionView: UICollectionView = {
        let collection = UICollectionView(
            frame: .zero,
            collectionViewLayout: collectionViewFlowLayout
        )
        collection.backgroundColor = .clear
        collection.register(ListViewCell.self, forCellWithReuseIdentifier: "ListViewCell")
        collection.translatesAutoresizingMaskIntoConstraints = false
        collection.showsHorizontalScrollIndicator = false
        collection.showsVerticalScrollIndicator = false
        collection.dataSource = self
        collection.delegate = self
        return collection
    }()
    
    // MARK: - Initialization
    
    init(model: Model, renderer: BeagleRenderer) {
        self.model = model
        self.renderer = renderer
        super.init(frame: .zero)
        setupViews()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupViews() {
        addSubview(collectionView)
        collectionView.anchorTo(superview: self)
    }
    
    override func layoutSubviews() {
        collectionView.reloadData()
        super.layoutSubviews()
    }
    
}

// MARK: - Model
extension ListViewUIComponent {
    struct Model {
        var listViewItems: [DynamicObject]?
        var direction: ListView.Direction
        var template: RawComponent
        var onScrollEnd: [RawAction]?
        var scrollThreshold: Int?
        var useParentScroll: Bool?
    }
}

// MARK: UICollectionViewDataSource

extension ListViewUIComponent: UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return model.listViewItems?.count ?? 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "ListViewCell", for: indexPath)
        if let cell = cell as? ListViewCell {
            contextResolver.reuse(cell: cell)
            cell.configure(item: indexPath.item, listView: self)
        }
        
        if validationSetOnScrollEnd {
            setOnScrollEnd(index: indexPath.row)
        }
        
        return cell
    }
    
}

// MARK: UICollectionViewDelegateFlowLayout

extension ListViewUIComponent: UICollectionViewDelegateFlowLayout {
        
    func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if let cell = cell as? ListViewCell {
            contextResolver.track(orphanCell: cell)
        }
    }
    
    func validatePercentage() -> Float {
        let percent = Float(model.scrollThreshold ?? 100)
        if percent > 100 && percent < 0 {
            return 1
        } else {
            return percent / 100
        }
    }
    
    func setOnScrollEnd(index: Int) {
        guard let action = model.onScrollEnd else { return }
        let numberOfItems = Float(model.listViewItems?.count ?? 0)
        let scrollThreshold = validatePercentage()
        let valuePercent = numberOfItems * scrollThreshold
        
        if Float(index) >= valuePercent {
            renderer.controller.execute(actions: action, origin: self)
            validationSetOnScrollEnd = false
        }
    }
    
}
