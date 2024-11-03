package lotto.vo;

import static lotto.constant.ExceptionMessage.AMOUNT_MUST_BE_NON_NEGATIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class MoneyTest {

    @Nested
    class 돈_생성_테스트 {
        @Test
        void 양의_금액으로_돈을_생성한다() {
            // given
            long amount = 1000;

            // when
            Money money = Money.from(amount);

            // then
            assertThat(money).isEqualTo(Money.from(1000));
        }

        @Test
        void 금액이_0인_돈을_생성한다() {
            // given
            long amount = 0;

            // when
            Money money = Money.from(amount);

            // then
            assertThat(money).isEqualTo(Money.from(0));
        }

        @ParameterizedTest
        @ValueSource(longs = {-1, -1000, Long.MIN_VALUE})
        void 음수_금액으로_돈을_생성할_수_없다(long amount) {
            assertThatThrownBy(() -> Money.from(amount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(AMOUNT_MUST_BE_NON_NEGATIVE.message());
        }
    }

    @Nested
    class 돈_더하기_테스트 {
        @ParameterizedTest
        @CsvSource({
                "1000, 2000, 3000",
                "0, 1000, 1000",
                "1000, 0, 1000",
                "0, 0, 0"
        })
        void 두_금액을_더한다(long amount1, long amount2, long expected) {
            // given
            Money money1 = Money.from(amount1);
            Money money2 = Money.from(amount2);

            // when
            Money result = money1.add(money2);

            // then
            assertThat(result).isEqualTo(Money.from(expected));
        }
    }

    @Nested
    class 돈_빼기_테스트 {
        @ParameterizedTest
        @CsvSource({
                "2000, 1000, 1000",
                "1000, 1000, 0",
                "1000, 0, 1000"
        })
        void 두_금액을_뺀다(long amount1, long amount2, long expected) {
            // given
            Money money1 = Money.from(amount1);
            Money money2 = Money.from(amount2);

            // when
            Money result = money1.subtract(money2);

            // then
            assertThat(result).isEqualTo(Money.from(expected));
        }

        @ParameterizedTest
        @CsvSource({
                "1000, 2000",
                "0, 1000",
                "1000, 1001"
        })
        void 뺀_결과가_음수가_되면_예외가_발생한다(long amount1, long amount2) {
            // given
            Money money1 = Money.from(amount1);
            Money money2 = Money.from(amount2);

            // when & then
            assertThatThrownBy(() -> money1.subtract(money2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(AMOUNT_MUST_BE_NON_NEGATIVE.message());
        }
    }

    @Nested
    class 나눗셈_가능_여부_테스트 {
        @ParameterizedTest
        @CsvSource({
                "1000, 1000, true",
                "2000, 1000, true",
                "1000, 3000, false",
                "0, 1000, true"
        })
        void 금액이_주어진_수로_나누어_떨어지는지_확인한다(long amount, long divisor, boolean expected) {
            // given
            Money money = Money.from(amount);

            // when
            boolean result = money.isDivisibleBy(divisor);

            // then
            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    class 동등성_비교_테스트 {
        @Test
        void 같은_금액을_가진_Money는_동등하다() {
            // given
            Money money1 = Money.from(1000);
            Money money2 = Money.from(1000);

            // then
            assertThat(money1)
                    .isEqualTo(money2)
                    .hasSameHashCodeAs(money2);
        }

        @Test
        void 다른_금액을_가진_Money는_동등하지_않다() {
            // given
            Money money1 = Money.from(1000);
            Money money2 = Money.from(2000);

            // then
            assertThat(money1)
                    .isNotEqualTo(money2)
                    .doesNotHaveSameHashCodeAs(money2);
        }

        @Test
        void Money와_다른_타입의_객체는_동등하지_않다() {
            // given
            Money money = Money.from(1000);
            Object other = new Object();

            // then
            assertThat(money).isNotEqualTo(other);
        }

        @Test
        void Money는_자기_자신과_동등하다() {
            // given
            Money money = Money.from(1000);

            // then
            assertThat(money).isEqualTo(money);
        }
    }
}