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

package br.com.zup.beagle.sample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.components.ListView
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.core.ListDirection
import br.com.zup.beagle.widget.core.Size

class ContextListViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = Screen(
            navigationBar = NavigationBar(title = "List"),
            child = buildListView()
        )
        return context?.let { declarative.toView(this) }
    }

    private val list = ListView(
        context = ContextData(
            id = "insideContext",
            value = listOf("1 inside", "2 inside", "3 inside", "4 inside", "5 inside",
                "6 inside", "7 inside", "8 inside", "9 inside", "10 inside",
                "11 inside", "12 inside", "13 inside", "14 inside", "15 inside",
                "16 inside", "17 inside", "18 inside", "19 inside", "20 inside"
            )
        ),
        dataSource = expressionOf("@{insideContext}"),
        direction = ListDirection.HORIZONTAL,
        template = Container(
            children = listOf(
                Text(text = expressionOf("@{item}")).applyStyle(
                    Style(
                        size = Size(width = 300.unitReal(), height = 80.unitReal())
                    )
                )
            )
        )
    )

    private fun buildListView() = ListView(
        context = ContextData(
            id = "outsideContext",
            value = listOf("1 outside", "2 outside", "3 outside", "4 outside", "5 outside",
                "6 outside", "7 outside", "8 outside", "9 outside", "10 outside")
        ),
        dataSource = expressionOf("@{outsideContext}"),
        direction = ListDirection.VERTICAL,
        template = Container(
            children = listOf(
                Text(text = expressionOf("@{item}")),
                list
            )
        ).applyStyle(
            Style(
                size = Size(width = 100.unitPercent(), height = 300.unitReal())
            )
        )
    )

    companion object {

        fun newInstance(): ContextListViewFragment {
            return ContextListViewFragment()
        }
    }
}