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
import br.com.zup.beagle.android.action.Alert
import br.com.zup.beagle.android.components.TextInput
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.widget.core.TextInputType

class TextInputFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = Screen(
            child = Container(
                children = listOf(
                    TextInput(
                        placeholder = "Text Input 1 with Actions",
                        disabled = false,
                        readOnly = true,
                        type = TextInputType.TEXT,
                        hidden = false,
                        styleId = "TextInput",
                        onChange = listOf(Alert(
                            "On Change",
                            "Text 1 Changed"
                        )),
                        onFocus = listOf(Alert(
                            "On Focus",
                            "Text 1 on focus"
                        )),
                        onBlur = listOf(Alert(
                            "On Blur",
                            "Text 1 on Blur"
                        ))

                    ),
                    TextInput(placeholder = "Text 2")

                )
            )
        )

        return declarative.toView(this)
    }

    companion object {
        fun newInstance(): TextInputFragment {
            return TextInputFragment()
        }
    }
}