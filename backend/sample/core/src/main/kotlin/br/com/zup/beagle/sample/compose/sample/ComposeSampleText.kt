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

package br.com.zup.beagle.sample.compose.sample

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.SCREEN_TEXT_STYLE
import br.com.zup.beagle.sample.constants.STEEL_BLUE
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Text

object ComposeSampleText: ComposeComponent {
    override fun build() = Container(
        children = listOf(
            beagleText(text = "hello world without style"),
            beagleText(text = "hello world with style", styleId = SCREEN_TEXT_STYLE),
            beagleText(text = "hello world with Appearance", appearanceColor = STEEL_BLUE),
            beagleText(
                text = "hello world with style and Appearance",
                styleId = SCREEN_TEXT_STYLE,
                appearanceColor = STEEL_BLUE
            )
        )
    )

    private fun beagleText(
        text: String,
        styleId: String? = null,
        appearanceColor: String? = null
    ) =
        Text(text = text, styleId = styleId)
            .applyStyle(Style(
                margin = EdgeValue(
                    top = 16.unitReal(),
                    left = 16.unitReal(),
                    right = 16.unitReal()
                ),
                backgroundColor = appearanceColor
            )
        )
}