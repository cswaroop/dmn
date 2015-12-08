(ns dmn.sample
  (:require [clara.rules :refer :all])
  (:require [schema.core :as s])))

;---------------
; Domain
;---------------

(s/defrecord Applicant 
    [name :- s/Str
     existingCustomerSoleAnnualIncomeAmount :- s/Num
     existingCustomerOutstandingMortgageBorrowingsAmount :- s/Num
     existingCustomerSavingAndInvestmentsBalanceAmount :- s/Num
     existingCustomerCurrentAccountType :- (s/enum :student 
                                                   :standard 
                                                   :silver 
                                                   :platinum 
                                                   :black 
                                                   :balance-transfer)
     yearsOfAge :- s/Int
     existingCustomer :- s/Bool
     soleAnnualIncomeAmount :- s/Num
     residentialStatus :- (s/enum :uk-resident :non-uk-resident)
     applicationCreditCardPreviouslyAppliedInLast6Months :- s/Bool
     numberOfYearsAddressHistory :- s/Int
     numberOfDefaultPaymentsInLast12Months :- s/Int
     numberofYearsAddressHistory :- s/Int
     declaredBankruptcy :- s/Bool
     yearsWithCurrentAccountBank :- s/Int
     amountOfAvailableCreditUsedPercentage :- s/Num
     ])

(s/defrecord Application 
    [cardType :- (s/enum :student 
                         :standard 
                         :silver 
                         :platinum 
                         :black 
                         :balance-transfer)
     creditScore :- s/Int
     creditCardEligibility :- (s/enum :eligible :ineligible)
     balanceTransferCreditCardEligibility :- (s/enum :eligible :ineligible)
     demographicSuitability :- (s/enum :eligible :ineligible)
     privateCreditCardDemographicSuitability :- (s/enum :eligible :ineligible)
     studentcreditCardDemographicSuitability :- (s/enum :eligible :ineligible)
     ])
