package com.sample.currencyconversion

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import androidx.test.espresso.IdlingRegistry
import com.sample.currencyconversion.helper.TestHelper
import com.sample.currencyconversion.util.ResourceIdleManager
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Before
    fun setUP() {
        // register resource idle
        IdlingRegistry.getInstance().register(ResourceIdleManager.countingIdleResources)
    }

    @After
    fun tearDown() {
        // unregister resource idle
        IdlingRegistry.getInstance().unregister(ResourceIdleManager.countingIdleResources)

    }

    @Test
    fun testDefaultCurrencyValue_equalsToOne() {
        // test the Text box is displayed
        composeTestRule.onNodeWithText("1")
            .assertExists()
            .assertIsDisplayed()
    }


    @Test
    fun testDefaultCurrencyCode_equalsToUSD() {
        // test the button is displayed
        composeTestRule.onNodeWithText("USD")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testCurrencyButton_isEnabledAfterApiCall() {
        composeTestRule.onNodeWithText("USD")
            .assertIsEnabled()
    }

    @Test
    fun testBottomSheet_isAppeared() {
        composeTestRule.onNodeWithText("AFN").assertIsDisplayed()
    }

    @Test
    fun testAllCurrency_To_ZeroValues() {

        val textFieldNode = composeTestRule.onNodeWithText("1") // Replace with appropriate matcher

        // replace value from the text filed and add 0
        textFieldNode.performTextReplacement("0")

        val gridNode = composeTestRule.onAllNodesWithText("0.0")[0]

        // assert the value 0.0 == 0.0
        assertThat(0.0, equalTo(TestHelper.getNumberFromSemantic(gridNode)))

    }

    @Test
    fun testAEDCurrency_to_10USDValues() {

        val newUSDCurrencyValue = 10.0
        val defaultAEDCurrencyValue: Double
        val newAEDCurrencyValue: Double

        // get AED default value based on USD 1
        val usd1BaseValue = composeTestRule.onAllNodesWithContentDescription("currency value")[0]

        defaultAEDCurrencyValue = TestHelper.getNumberFromSemantic(usd1BaseValue)

        // find the BasicTextField with 1 USD
        val textFieldNode = composeTestRule.onNodeWithText("1") // Replace with appropriate matcher

        // replace 1 with value 10  USD
        textFieldNode.performTextReplacement("10")

        // get new update AED node according to 10 USD
        val gridNode = composeTestRule.onAllNodesWithContentDescription("currency value")[0]

        // get update AED double value
        newAEDCurrencyValue = TestHelper.getNumberFromSemantic(gridNode)

        // check expected value by multiplying new USD value ($10) * default AED value
        val expectedAEDCurrencyValue = newUSDCurrencyValue * defaultAEDCurrencyValue

        // assert
        assertThat(expectedAEDCurrencyValue, equalTo(newAEDCurrencyValue))  // Using equalTo matcher

    }

    @Test
    fun testAUDCurrency_to_100USDValues() {

        val newUSDCurrencyValue = 100.0
        val defaultAUDCurrencyValue: Double
        val newAUDCurrencyValue: Double

        // get AUD default value based on USD 1
        val aud1BaseNode = composeTestRule.onAllNodesWithContentDescription("currency value")[7]

        defaultAUDCurrencyValue = TestHelper.getNumberFromSemantic(aud1BaseNode)

        // find the BasicTextField with 1 USD
        val textFieldNode = composeTestRule.onNodeWithText("1") // Replace with appropriate matcher
        // replace 1 with value 100 USD
        textFieldNode.performTextReplacement("100")

        // get new update AUD node according to 100 USD
        val cellEightNode = composeTestRule.onAllNodesWithContentDescription("currency value")[7]

        // get updated AED double value
        newAUDCurrencyValue = TestHelper.getNumberFromSemantic(cellEightNode)

        // check expected value by multiplying new USD value ($100) * default AUD value
        val expectedAEDCurrencyValue = newUSDCurrencyValue * defaultAUDCurrencyValue
        // assert
        assertThat(expectedAEDCurrencyValue, equalTo(newAUDCurrencyValue))

    }

    @Test
    fun testChooseCurrencyCode_UpdateButtonText() {

        val selectedCurrencyCode: String
        val buttonCurrencyCode: String
        // click currency select button
        composeTestRule.onNodeWithText("USD")
            .performClick()

        val bottomSheetNode = composeTestRule.onAllNodesWithContentDescription("currency code")[0]

        // get the bottom sheet value in zero index
        selectedCurrencyCode = TestHelper.getTextFromSemantic(bottomSheetNode)

        // click on a bottom sheet row
        bottomSheetNode.performClick()

        val currencyButtonNode = composeTestRule.onNodeWithContentDescription("currency button")

        // get the updated button label
        buttonCurrencyCode = TestHelper.getTextFromSemantic(currencyButtonNode)

        // currencyButton.
        assertThat(selectedCurrencyCode, equalTo(buttonCurrencyCode))

    }

    @Test
    fun testBaseCurrency_to_AUD_UpdateAED(){

        val previousAUDValue:Double
        val previousAEDValue:Double
        val updatedAEDValue:Double

        // click currency select button
        composeTestRule.onNodeWithText("USD")
            .performClick()

        // get previous AUD currency value from grid
        val audGridNode = composeTestRule.onAllNodesWithContentDescription("currency value")[7]
        previousAUDValue = TestHelper.getNumberFromSemantic(audGridNode)

        // get previous AED currency value from grid
        val aedGridNode = composeTestRule.onAllNodesWithContentDescription("currency value")[0]
        previousAEDValue = TestHelper.getNumberFromSemantic(aedGridNode)

        // get bottom sheet first node
        val bottomSheetNode = composeTestRule.onAllNodesWithContentDescription("currency code")[0]
        val audNode = composeTestRule.onAllNodesWithContentDescription("currency code")[7]
        bottomSheetNode.performScrollTo()

        audNode.performClick()

        // get updated AED currency value from grid
        val aedNewGridNode = composeTestRule.onAllNodesWithContentDescription("currency value")[0]
        updatedAEDValue = TestHelper.getNumberFromSemantic(aedNewGridNode)

        val expectedAEDConversion:Double = previousAEDValue / previousAUDValue

        // currencyButton.
        assertThat(expectedAEDConversion, equalTo(updatedAEDValue))

    }

}