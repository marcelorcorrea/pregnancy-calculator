import com.marcelorcorrea.pregnancycalculator.service.PregnancyCalculatorService
import com.marcelorcorrea.pregnancycalculator.util.TimeProvider
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PregnancyCalculatorServiceSpec : Spek({
    val pregnancyCalculatorService by memoized { PregnancyCalculatorService() }

    beforeGroup {
        TimeProvider.useFixedClockAt(LocalDate.of(2019, 11, 26))
    }

    describe("#${PregnancyCalculatorService::calculatePeriodFrom.name}") {
        val period by memoized { pregnancyCalculatorService.calculatePeriodFrom(30L) }

        it("should convert 30 weeks to 6 months and 27 days") {
            assertEquals(27, period.days)
            assertEquals(6, period.months)
        }
    }

    describe("#${PregnancyCalculatorService::calculateDateOfConception.name}") {
        val dueDate = LocalDate.of(2020, 8, 15)
        val dateOfConception by memoized { pregnancyCalculatorService.calculateDateOfConception(dueDate) }

        it("should be 23/11/2019 ") {
            assertEquals(23, dateOfConception.dayOfMonth)
            assertEquals(11, dateOfConception.monthValue)
            assertEquals(2019, dateOfConception.year)

        }
    }

    describe("#${PregnancyCalculatorService::calculateDateOfLastMenstrualPeriod.name}") {
        val dateOfConception = LocalDate.of(2019, 11, 23)
        val lastMenstrualPeriod by memoized {
            pregnancyCalculatorService.calculateDateOfLastMenstrualPeriod(
                dateOfConception
            )
        }
        it("should be 09/11/2019") {
            assertEquals(9, lastMenstrualPeriod.dayOfMonth)
            assertEquals(11, lastMenstrualPeriod.monthValue)
            assertEquals(2019, lastMenstrualPeriod.year)
        }
    }

    describe("#${PregnancyCalculatorService::calculateGestationalAge.name}") {
        val lastMenstrualPeriod = LocalDate.of(2019, 11, 9)
        val gestationalAge by memoized { pregnancyCalculatorService.calculateGestationalAge(lastMenstrualPeriod) }

        it("shoud be 2 weeks and 3 days") {
            assertEquals(2, gestationalAge.weeks)
            assertEquals(3, gestationalAge.days)
        }
    }

    describe("#${PregnancyCalculatorService::calculateDueDate.name}") {
        val lastMenstrualPeriod = LocalDate.of(2019, 11, 9)
        val dueDate by memoized { pregnancyCalculatorService.calculateDueDate(lastMenstrualPeriod) }
        it("should be 15/8/2020") {
            assertEquals(15, dueDate.dayOfMonth)
            assertEquals(8, dueDate.monthValue)
            assertEquals(2020, dueDate.year)
        }
    }

    describe("#${PregnancyCalculatorService::isDueDateExceedsLimit.name}") {
        context("When due date exceeds the maximum due date limit") {
            val dueDate = LocalDate.of(2021, 8, 15)
            val result by memoized { pregnancyCalculatorService.isDueDateExceedsLimit(dueDate) }

            it("should return true") {
                assertTrue(result)
            }
        }
        context("When due date exceeds the minimum due date limit") {
            val dueDate = LocalDate.of(2019, 8, 15)
            val result by memoized { pregnancyCalculatorService.isDueDateExceedsLimit(dueDate) }

            it("should return true") {
                assertTrue(result)
            }
        }
        context("When due date is acceptable") {
            val dueDate = LocalDate.of(2020, 8, 15)
            val result by memoized { pregnancyCalculatorService.isDueDateExceedsLimit(dueDate) }

            it("should return false") {
                assertFalse(result)
            }
        }
    }

    describe("#${PregnancyCalculatorService::isLastMenstrualPeriodExceedsLimit.name}") {

        context("When lastMenstrualPeriod exceeds the minimum date of conception limit") {
            val lastMenstrualPeriod = LocalDate.of(2021, 8, 15)
            val result by memoized { pregnancyCalculatorService.isLastMenstrualPeriodExceedsLimit(lastMenstrualPeriod) }

            it("should return true") {
                assertTrue(result)
            }
        }
        context("When lastMenstrualPeriod exceeds the maximum date of conception limit") {
            val lastMenstrualPeriod = LocalDate.of(2019, 1, 15)
            val result by memoized { pregnancyCalculatorService.isLastMenstrualPeriodExceedsLimit(lastMenstrualPeriod) }

            it("should return true") {
                assertTrue(result)
            }
        }
        context("When lastMenstrualPeriod is acceptable") {
            val lastMenstrualPeriod = LocalDate.of(2019, 8, 15)
            val result by memoized { pregnancyCalculatorService.isLastMenstrualPeriodExceedsLimit(lastMenstrualPeriod) }

            it("should return false") {
                assertFalse(result)
            }
        }
    }
})
