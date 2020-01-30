import com.hp.kalexa.model.Context
import com.hp.kalexa.model.Session
import com.hp.kalexa.model.Slot
import com.hp.kalexa.model.request.AlexaRequest
import com.hp.kalexa.model.request.IntentRequest
import com.marcelorcorrea.pregnancycalculator.intent.DueDateIntent
import com.marcelorcorrea.pregnancycalculator.intent.TestUtil
import com.marcelorcorrea.pregnancycalculator.service.PregnancyCalculatorService
import com.marcelorcorrea.pregnancycalculator.util.I18nResource
import com.marcelorcorrea.pregnancycalculator.util.Util.toLocalizedString
import io.mockk.every
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.time.LocalDate
import java.util.Locale
import kotlin.test.assertEquals

class DueDateIntentSpec : Spek({

    val pregnancyCalculatorService by memoized { mockk<PregnancyCalculatorService>() }
    val dueDateIntent: DueDateIntent by memoized { DueDateIntent(pregnancyCalculatorService) }

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
    }

    describe("when provided a valid date of conception") {
        val dateOfConception = LocalDate.of(2019, 11, 23)
        val dueDate = LocalDate.of(2020, 8, 15)
        beforeEachTest {
            every { pregnancyCalculatorService.calculateDueDate(any()) } returns dueDate
            every { pregnancyCalculatorService.isLastMenstrualPeriodExceedsLimit(any()) } returns false
            every { slot.value } returns dateOfConception.toString()
        }

        it("should return the due date correctly") {
            val response = dueDateIntent.onIntentRequest(alexaRequest)
            assertEquals(
                i18nResource.getString("due_date_msg").format(
                    dueDate.toLocalizedString(locale)
                ),
                TestUtil.getOutputSpeechText(response)
            )
        }
    }

    describe("when the date of conception is invalid") {
        beforeEachTest {
            every { slot.value } returns LocalDate.now().plusWeeks(41).toString()
            every { pregnancyCalculatorService.isLastMenstrualPeriodExceedsLimit(any()) } returns true
        }
        it("should return limit exceeded error msg") {
            val response = dueDateIntent.onIntentRequest(alexaRequest)
            assertEquals(i18nResource.getString("date_limit_exceeded"), TestUtil.getOutputSpeechText(response))
        }
    }
})
