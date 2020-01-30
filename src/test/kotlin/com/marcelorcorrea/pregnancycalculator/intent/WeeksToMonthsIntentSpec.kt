import com.hp.kalexa.model.Context
import com.hp.kalexa.model.Session
import com.hp.kalexa.model.Slot
import com.hp.kalexa.model.request.AlexaRequest
import com.hp.kalexa.model.request.IntentRequest
import com.marcelorcorrea.pregnancycalculator.intent.TestUtil
import com.marcelorcorrea.pregnancycalculator.intent.WeeksToMonthsIntent
import com.marcelorcorrea.pregnancycalculator.service.PregnancyCalculatorService
import com.marcelorcorrea.pregnancycalculator.util.I18nResource
import io.mockk.every
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.time.Period
import java.util.Locale
import kotlin.test.assertEquals

class WeeksToMonthsIntentSpec : Spek({

    val pregnancyCalculatorService by memoized { mockk<PregnancyCalculatorService>() }
    val weeksToMonthsIntent: WeeksToMonthsIntent by memoized { WeeksToMonthsIntent(pregnancyCalculatorService) }

    val alexaRequest = mockk<AlexaRequest<IntentRequest>>()
    val slot = mockk<Slot>()
    val session = mockk<Session>()
    val context = mockk<Context>()
    val intentRequest = mockk<IntentRequest>()
    val locale: Locale = Locale.forLanguageTag("pt-BR")
    val i18nResource = I18nResource()

    beforeGroup {
        i18nResource.load(locale)
        val requestAttributes: MutableMap<String, Any> = mutableMapOf("locale" to locale, "i18n" to i18nResource)
        val version = "1.0"
        every { alexaRequest.session } returns session
        every { alexaRequest.context } returns context
        every { alexaRequest.version } returns version
        every { alexaRequest.request } returns intentRequest
        every { session.attributes } returns mutableMapOf()
        every { alexaRequest.requestAttributes } returns requestAttributes
        every { alexaRequest.request.intent.getSlot(any()) } returns slot
        every { slot.value } returns "20"
    }

    describe("when converting weeks to months") {

        context("when the response is more than one month and more than one day") {
            val period = mockk<Period>()
            beforeEachTest {
                every { period.months } returns 7
                every { period.days } returns 24
                every { pregnancyCalculatorService.calculatePeriodFrom(any()) } returns period
            }

            it("should return 'months_and_days' message") {
                val response = weeksToMonthsIntent.onIntentRequest(alexaRequest)
                assertEquals(
                    i18nResource.getString("months_and_days").format(
                        period.months,
                        period.days
                    ),
                    TestUtil.getOutputSpeechText(response)
                )
            }
        }
        context("when the response is one month and one day") {
            val period = mockk<Period>()
            beforeEachTest {
                every { period.months } returns 1
                every { period.days } returns 1
                every { pregnancyCalculatorService.calculatePeriodFrom(any()) } returns period
            }

            it("should return 'one_month_one_day' message") {
                val response = weeksToMonthsIntent.onIntentRequest(alexaRequest)
                assertEquals(
                    i18nResource.getString("one_month_one_day"),
                    TestUtil.getOutputSpeechText(response)
                )
            }
        }
        context("when the response is one month and more than one day") {
            val period = mockk<Period>()
            beforeEachTest {
                every { period.months } returns 1
                every { period.days } returns 24
                every { pregnancyCalculatorService.calculatePeriodFrom(any()) } returns period
            }

            it("should return 'one_month_days' message") {
                val response = weeksToMonthsIntent.onIntentRequest(alexaRequest)
                assertEquals(
                    i18nResource.getString("one_month_days").format(
                        period.days
                    ),
                    TestUtil.getOutputSpeechText(response)
                )
            }
        }
        context("when the response is more than one month") {
            val period = mockk<Period>()
            beforeEachTest {
                every { period.months } returns 7
                every { period.days } returns 0
                every { pregnancyCalculatorService.calculatePeriodFrom(any()) } returns period
            }

            it("should return 'months' message") {
                val response = weeksToMonthsIntent.onIntentRequest(alexaRequest)
                assertEquals(
                    i18nResource.getString("months").format(
                        period.months
                    ),
                    TestUtil.getOutputSpeechText(response)
                )
            }
        }
        context("when the response is more than one month and one day") {
            val period = mockk<Period>()
            beforeEachTest {
                every { period.months } returns 7
                every { period.days } returns 1
                every { pregnancyCalculatorService.calculatePeriodFrom(any()) } returns period
            }

            it("should return 'months_one_day' message") {
                val response = weeksToMonthsIntent.onIntentRequest(alexaRequest)
                assertEquals(
                    i18nResource.getString("months_one_day").format(
                        period.months
                    ),
                    TestUtil.getOutputSpeechText(response)
                )
            }
        }
        context("when the response is  one month ") {
            val period = mockk<Period>()
            beforeEachTest {
                every { period.months } returns 1
                every { period.days } returns 0
                every { pregnancyCalculatorService.calculatePeriodFrom(any()) } returns period
            }

            it("should return 'month' message") {
                val response = weeksToMonthsIntent.onIntentRequest(alexaRequest)
                assertEquals(
                    i18nResource.getString("month"),
                    TestUtil.getOutputSpeechText(response)
                )
            }
        }
        context("when the response is more than one day") {
            val period = mockk<Period>()
            beforeEachTest {
                every { period.months } returns 0
                every { period.days } returns 24
                every { pregnancyCalculatorService.calculatePeriodFrom(any()) } returns period
            }

            it("should return 'days' message") {
                val response = weeksToMonthsIntent.onIntentRequest(alexaRequest)
                assertEquals(
                    i18nResource.getString("days").format(
                        period.days
                    ),
                    TestUtil.getOutputSpeechText(response)
                )
            }
        }
        context("when the response is one day") {
            val period = mockk<Period>()
            beforeEachTest {
                every { period.months } returns 0
                every { period.days } returns 1
                every { pregnancyCalculatorService.calculatePeriodFrom(any()) } returns period
            }

            it("should return 'one_day' message") {
                val response = weeksToMonthsIntent.onIntentRequest(alexaRequest)
                assertEquals(
                    i18nResource.getString("one_day"),
                    TestUtil.getOutputSpeechText(response)
                )
            }
        }
    }
})
