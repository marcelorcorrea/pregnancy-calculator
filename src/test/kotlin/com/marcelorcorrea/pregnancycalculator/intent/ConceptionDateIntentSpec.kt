import com.hp.kalexa.model.Context
import com.hp.kalexa.model.Session
import com.hp.kalexa.model.Slot
import com.hp.kalexa.model.request.AlexaRequest
import com.hp.kalexa.model.request.IntentRequest
import com.marcelorcorrea.pregnancycalculator.intent.ConceptionDateIntent
import com.marcelorcorrea.pregnancycalculator.intent.TestUtil.getOutputSpeechText
import com.marcelorcorrea.pregnancycalculator.model.GestationalAge
import com.marcelorcorrea.pregnancycalculator.service.PregnancyCalculatorService
import com.marcelorcorrea.pregnancycalculator.util.I18nResource
import com.marcelorcorrea.pregnancycalculator.util.Util.toLocalizedString
import io.mockk.every
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals

class ConceptionDateIntentSpec : Spek({
    val pregnancyCalculatorService by memoized { mockk<PregnancyCalculatorService>() }
    val conceptionDateIntent: ConceptionDateIntent by memoized { ConceptionDateIntent(pregnancyCalculatorService) }

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
        i18nResource.load(locale)
    }

    describe("when provided a valid due date") {
        val dueDate = LocalDate.of(2020, 8, 15)
        val dateOfConception = LocalDate.of(2019, 11, 23)
        val lastPeriod = dateOfConception.minusDays(2)
        lateinit var gestationalAge: GestationalAge
        beforeEachTest {
            gestationalAge = GestationalAge(33, 4)
            every { pregnancyCalculatorService.calculateDateOfConception(any()) } returns dateOfConception
            every { pregnancyCalculatorService.calculateDateOfLastMenstrualPeriod(any()) } returns lastPeriod
            every { pregnancyCalculatorService.calculateGestationalAge(any()) } returns gestationalAge
            every { pregnancyCalculatorService.isDueDateExceedsLimit(any()) } returns false
            every { slot.value } returns dueDate.toString()
        }
        context("when gestation age is more than one week and more than one day") {
            it("should return 'weeks_and_days' message of gestational age") {
                val response = conceptionDateIntent.onIntentRequest(alexaRequest)
                val weeksAndDays = i18nResource.getString("weeks_and_days").format(gestationalAge.weeks, gestationalAge.days)
                assertEquals(
                        i18nResource.getString("date_of_conception_msg").format(
                                lastPeriod.toLocalizedString(locale),
                                dateOfConception.toLocalizedString(locale),
                                weeksAndDays
                        ),
                        getOutputSpeechText(response)
                )
            }
        }
        context("when gestational age is one week") {
            beforeEachTest {
                gestationalAge = GestationalAge(1)
                every { pregnancyCalculatorService.calculateGestationalAge(any()) } returns gestationalAge
            }
            it("should return 'one_week' message of gestational age") {
                val response = conceptionDateIntent.onIntentRequest(alexaRequest)
                val oneWeekMsg = i18nResource.getString("one_week")
                assertEquals(
                        i18nResource.getString("date_of_conception_msg").format(
                                lastPeriod.toLocalizedString(locale),
                                dateOfConception.toLocalizedString(locale),
                                oneWeekMsg
                        ),
                        getOutputSpeechText(response)
                )
            }
        }
        context("when gestational age is more than one week") {
            beforeEachTest {
                gestationalAge = GestationalAge(25)
                every { pregnancyCalculatorService.calculateGestationalAge(any()) } returns gestationalAge
            }
            it("should return 'weeks' message of gestational age") {
                val response = conceptionDateIntent.onIntentRequest(alexaRequest)
                val weeksMsg = i18nResource.getString("weeks").format(gestationalAge.weeks)
                assertEquals(
                        i18nResource.getString("date_of_conception_msg").format(
                                lastPeriod.toLocalizedString(locale),
                                dateOfConception.toLocalizedString(locale),
                                weeksMsg
                        ),
                        getOutputSpeechText(response)
                )
            }
        }
        context("when gestational age is more than one week and one day") {
            beforeEachTest {
                gestationalAge = GestationalAge(25, 1)
                every { pregnancyCalculatorService.calculateGestationalAge(any()) } returns gestationalAge
            }
            it("should return 'weeks_one_day' message of gestational age") {
                val response = conceptionDateIntent.onIntentRequest(alexaRequest)
                val weeksOneDay = i18nResource.getString("weeks_one_day").format(gestationalAge.weeks, gestationalAge.days)
                assertEquals(
                        i18nResource.getString("date_of_conception_msg").format(
                                lastPeriod.toLocalizedString(locale),
                                dateOfConception.toLocalizedString(locale),
                                weeksOneDay
                        ),
                        getOutputSpeechText(response)
                )
            }
        }
        context("when gestational age is one week and one day") {
            beforeEachTest {
                gestationalAge = GestationalAge(1, 1)
                every { pregnancyCalculatorService.calculateGestationalAge(any()) } returns gestationalAge
            }
            it("should return 'one_week_one_day' message of gestational age") {
                val response = conceptionDateIntent.onIntentRequest(alexaRequest)
                val oneWeekOneDay = i18nResource.getString("one_week_one_day")
                assertEquals(
                        i18nResource.getString("date_of_conception_msg").format(
                                lastPeriod.toLocalizedString(locale),
                                dateOfConception.toLocalizedString(locale),
                                oneWeekOneDay
                        ),
                        getOutputSpeechText(response)
                )
            }
        }
        context("when gestational age is one week and more than one day") {
            beforeEachTest {
                gestationalAge = GestationalAge(1, 7)
                every { pregnancyCalculatorService.calculateGestationalAge(any()) } returns gestationalAge
            }
            it("should return 'one_week_days' message of gestational age") {
                val response = conceptionDateIntent.onIntentRequest(alexaRequest)
                val oneWeekDays = i18nResource.getString("one_week_days").format(gestationalAge.days)
                assertEquals(
                        i18nResource.getString("date_of_conception_msg").format(
                                lastPeriod.toLocalizedString(locale),
                                dateOfConception.toLocalizedString(locale),
                                oneWeekDays
                        ),
                        getOutputSpeechText(response)
                )
            }
        }

        context("when gestational age is one day") {
            beforeEachTest {
                gestationalAge = GestationalAge(0, 1)
                every { pregnancyCalculatorService.calculateGestationalAge(any()) } returns gestationalAge
            }
            it("should return message showing one week of gestational age") {
                val response = conceptionDateIntent.onIntentRequest(alexaRequest)
                val oneDay = i18nResource.getString("one_day")
                assertEquals(
                        i18nResource.getString("date_of_conception_msg").format(
                                lastPeriod.toLocalizedString(locale),
                                dateOfConception.toLocalizedString(locale),
                                oneDay
                        ),
                        getOutputSpeechText(response)
                )
            }
        }
        context("when gestational age is more than one day") {
            beforeEachTest {
                gestationalAge = GestationalAge(0, 10)
                every { pregnancyCalculatorService.calculateGestationalAge(any()) } returns gestationalAge
            }
            it("should return 'days' message of gestational age") {
                val response = conceptionDateIntent.onIntentRequest(alexaRequest)
                val days = i18nResource.getString("days").format(gestationalAge.days)
                assertEquals(
                        i18nResource.getString("date_of_conception_msg").format(
                                lastPeriod.toLocalizedString(locale),
                                dateOfConception.toLocalizedString(locale),
                                days
                        ),
                        getOutputSpeechText(response)
                )
            }
        }
    }

    describe("when the due date is invalid") {
        beforeEachTest {
            every { slot.value } returns LocalDate.now().plusWeeks(41).toString()
            every { pregnancyCalculatorService.isDueDateExceedsLimit(any()) } returns true
        }
        it("should return limit exceeded error msg") {
            val response = conceptionDateIntent.onIntentRequest(alexaRequest)
            assertEquals(i18nResource.getString("date_limit_exceeded"), getOutputSpeechText(response))
        }
    }
})
