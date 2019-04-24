/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.ui.material

import androidx.test.filters.MediumTest
import androidx.ui.core.CraneWrapper
import androidx.ui.core.TestTag
import androidx.ui.layout.Column
import androidx.ui.test.android.AndroidUiTestRunner
import androidx.ui.test.assertIsInMutuallyExclusiveGroup
import androidx.ui.test.assertIsSelected
import androidx.ui.test.assertSemanticsIsEqualTo
import androidx.ui.test.copyWith
import androidx.ui.test.createFullSemantics
import androidx.ui.test.doClick
import androidx.ui.test.findByTag
import com.google.r4a.Children
import com.google.r4a.Composable
import com.google.r4a.Model
import com.google.r4a.composer
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@Model
internal class RadioGroupSelectedState<T>(var selected: T)

@MediumTest
@RunWith(JUnit4::class)
class RadioGroupUiTest : AndroidUiTestRunner() {

    private val itemOne = "Bar"
    private val itemTwo = "Foo"
    private val itemThree = "Sap"

    private val unselectedRadioGroupItemSemantics = createFullSemantics(
        inMutuallyExclusiveGroup = true,
        isSelected = false
    )
    private val selectedRadioGroupItemSemantics = unselectedRadioGroupItemSemantics.copyWith {
        isSelected = true
    }
    private val options = listOf(itemOne, itemTwo, itemThree)

    @Composable
    fun VerticalRadioGroupforTests(@Children children: RadioGroupScope.() -> Unit) {
        <CraneWrapper>
            <MaterialTheme>
                <RadioGroup>
                    <Column>
                        <children p1=this />
                    </Column>
                </RadioGroup>
            </MaterialTheme>
        </CraneWrapper>
    }

    @Test
    fun radioGroupTest_defaultSemantics() {
        val select = RadioGroupSelectedState(itemOne)

        setContent {
            <VerticalRadioGroupforTests> options.forEach { item ->
                <TestTag tag=item>
                    <RadioGroupTextItem
                        text=item
                        selected=(select.selected == item)
                        onSelected={ select.selected = item } />
                </TestTag>
            }
            </VerticalRadioGroupforTests>
        }

        findByTag(itemOne).assertSemanticsIsEqualTo(selectedRadioGroupItemSemantics)
        findByTag(itemTwo).assertSemanticsIsEqualTo(unselectedRadioGroupItemSemantics)
        findByTag(itemThree).assertSemanticsIsEqualTo(unselectedRadioGroupItemSemantics)

        findByTag(itemOne)
            .assertIsInMutuallyExclusiveGroup()
            .assertIsSelected(true)
        findByTag(itemTwo)
            .assertIsInMutuallyExclusiveGroup()
            .assertIsSelected(false)
        findByTag(itemThree)
            .assertIsInMutuallyExclusiveGroup()
            .assertIsSelected(false)
    }

    @Test
    fun radioGroupTest_ensureUnselectable() {
        val select = RadioGroupSelectedState(itemOne)

        setContent {
            <VerticalRadioGroupforTests> options.forEach { item ->
                <TestTag tag=item>
                    <RadioGroupTextItem
                        text=item
                        selected=(select.selected == item)
                        onSelected={ select.selected = item } />
                </TestTag>
            }
            </VerticalRadioGroupforTests>
        }

        findByTag(itemOne)
            .assertSemanticsIsEqualTo(selectedRadioGroupItemSemantics)
            .doClick()
            .assertSemanticsIsEqualTo(selectedRadioGroupItemSemantics)

        findByTag(itemTwo)
            .assertSemanticsIsEqualTo(unselectedRadioGroupItemSemantics)

        findByTag(itemThree)
            .assertSemanticsIsEqualTo(unselectedRadioGroupItemSemantics)
    }

    @Test
    fun radioGroupTest_clickSelect() {
        val select = RadioGroupSelectedState(itemOne)
        setContent {
            <VerticalRadioGroupforTests> options.forEach { item ->
                <TestTag tag=item>
                    <RadioGroupTextItem
                        text=item
                        selected=(select.selected == item)
                        onSelected={ select.selected = item } />
                </TestTag>
            }
            </VerticalRadioGroupforTests>
        }
        findByTag(itemTwo)
            .assertSemanticsIsEqualTo(unselectedRadioGroupItemSemantics)
            .doClick()
            .assertSemanticsIsEqualTo(selectedRadioGroupItemSemantics)

        findByTag(itemOne)
            .assertSemanticsIsEqualTo(unselectedRadioGroupItemSemantics)

        findByTag(itemThree)
            .assertSemanticsIsEqualTo(unselectedRadioGroupItemSemantics)
    }

    @Test
    fun radioGroupTest_clickSelectTwoDifferentItems() {
        val select = RadioGroupSelectedState(itemOne)

        setContent {
            <VerticalRadioGroupforTests> options.forEach { item ->
                <TestTag tag=item>
                    <RadioGroupTextItem
                        text=item
                        selected=(select.selected == item)
                        onSelected={ select.selected = item } />
                </TestTag>
            }
            </VerticalRadioGroupforTests>
        }

        findByTag(itemTwo)
            .assertSemanticsIsEqualTo(unselectedRadioGroupItemSemantics)
            .doClick()
            .assertSemanticsIsEqualTo(selectedRadioGroupItemSemantics)

        findByTag(itemOne)
            .assertSemanticsIsEqualTo(unselectedRadioGroupItemSemantics)

        findByTag(itemThree)
            .assertSemanticsIsEqualTo(unselectedRadioGroupItemSemantics)
            .doClick()
            .assertSemanticsIsEqualTo(selectedRadioGroupItemSemantics)

        findByTag(itemOne)
            .assertSemanticsIsEqualTo(unselectedRadioGroupItemSemantics)

        findByTag(itemTwo)
            .assertSemanticsIsEqualTo(unselectedRadioGroupItemSemantics)
    }
}