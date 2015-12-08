(ns dmn.domain
  (:require [schema.core :as s]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; DM structures
;;

(def DMCreditLine
  "personal credit line"
  {:creditLineFlag  s/Bool
   :creditLineLimit  s/Num
   :totalPotentialAggregateExposureUSD s/Num
   :numberofPotentialAssets s/Int
   :retrievedDateTime s/Str
   :potentialTrueCommercialFlag s/Bool
   :status s/Str
   :lastReviewDate s/Str
   :expiryDate  java.util.Date})

(s/def DMExperience 
  {:id :- s/Int
   :totalAmountFinanced :- s/Num
   :totalOutstandingBalance :- s/Num
   :retrievedDateTime
   :PIFActiveIdDate
   :NumberOfTimes30to59
   :NumberofTimes60to89
   :NumberofTimesGT90
   :SettlementFlag
   :Payment})

(s/def DMPreviousBureau 
  {:id
   :bureauType
   :bureauName
   :bureauPullDate
   :isBureauValid
   :isBureauActive
   :isKeyDataMatch
   :isPrimaryBureau
   :isManualBureauPull
   :keyDate {:firstName
             :lastName
             :dateOfBirth
             :postalCode  s/Str
             }
   :characteristic {:code s/Str :value s/Str}
   })

(s/def DMCommercialBureau
  {:Id :- s/Str
   :bureauName :- s/Str
   :CompanyName :- s/Str
   :CompanyRegistrationNumber :- s/Str
   :bureauPullDate :- s/Date
   :isManualBureauPull :- s/Bool
   :isBureauValid :- s/Bool
   :isKeyDataMatch :- s/Bool
   :isPrimaryBureau :- s/Bool
   :ScoreGrade :- s/Str
   :WorkingCapital :- s/Num
   :CompanyInsolvencyFlag :- s/Bool
   :GazetteInformationFlag :- s/Bool
   :LocationCCJFlag :- s/Bool
   :CommercialCIFASFlag :- s/Bool
   :TangibleNetWorth :- s/Bool
   })

(s/def DMPreviousApplications 
  {:id :- s/Str
   :totalAmountFinanced :- s/Num
   :applicantStatus :- s/Str
   })

(s/def DMPersonalApplicant 
  {:id
   :type
   :isTrueCommercialCustomer
   :trueCommercialExposure
   :trueCommercialDealer
   :isCreditLineCustomer
   :lowerLevelCLFlag
   :dti
   :pti
   :ltv
   :scoreOdds
   :riskTier
   :CBScore
   :GMFScore
   :CIFASIndirectFlag
   :HMTFlag
   :AssociateAdverseCreditFlag
   :UndisclosedAddressesFlag
   :BankruptcyFlag
   :BankruptcyIndicator
   :DoBMismatchFlag
   :FraudRuleFlag
   :CashAdvanceIndicator
   :CCJIndicator
   :IVAIndicator
   :ExceptionCount
   :RedExceptionCount
   :aggregateExposure
   :aggregateExposureUSD
   :aggregatePotential
   :TotalGMFLending
   :TotalOSApplications
   :FileVariation
   :GMFExpTotalMonthlyPayment
   :TotalUnsecuredMonthlyPayment
   :homeTelephoneNumber
   :workPhoneNumber
   :mobilePhoneNumber
   :isExistingCustomer
   :monthsWithPresentEmployer
   :monthsAtCurrentAddress
   :gender
   :age
   :directorTAoDecision
   :guaranteeAvailable
   :useOfCreditInfo
   :isNewBureauRequired
   :futureObligations
   :applicantBankDetail {}
   :applicantEmployment [{}]
   :applicantAddress [{}]
   :addressLocation {}
   :applicantScore {}
   :person {:birthDate
            :numberOfDependents
            :emailAddress
            :mailingAddress
            :name {:prefix
                   :first
                   :last}}
   :isNewBureauRequired   :- s/Bool
   :requiredBureauInfo    :- s/Bool
   :previousBureau        :- [DMPreviousBureau]
   :gmfExperience         :- DMExperience
   :previoiusApplications :- DMPreviousApplications
   :policyAndDLA :- {:dynamicRuleDetail :- DMDynamicRuleDetail}
   :complianceCriteria :- {:dynamicRuleDetail :- DMDynamicRuleDetail}
   :fraudCriteria  :- {:dynamicRuleDetail :- DMDynamicRuleDetail} 
})
 
(s/def DMProductRequest 
  {:ProductCategory :- s/Str
   :ProductCode :- s/Str
   :DMProduct :- s/Str
   :DMFunction :- s/Str
   }) 

(s/def DMCreditRequest 
  {:ProductCategory :- s/Str
   :ProductCode :- s/Str
   :DMProduct :- s/Str
   :DMFunction :- s/Str
   :ProductRequestList :- [ProductRequest]
   })

(s/def DMDynamicRuleDetail 
  [{:orderNo  :- s/Str
    :ruleName  :- s/Str
    :currentValue  :- s/Str
    :delta  :- s/Str
    :limitValue  :- s/Str
    :limit  :- s/Str
    :priority :- s/Str
    }])

(s/def DMFinancialDetails 
  {:term
   :APR
   :FDA
   :CashDeposit
   :TotalDeposit
   :tradeInEquity
   :monthlyPayment
   :totalAmountFinanced
   :Advance
   :vehicleCashSellingPrice
   :VAPS
   :flatRate
   :Frequency
   :systemResidualValue
   :annualMileage
   :residualValue
   :financePlan
   :settlement
   :totalDownPayment
   :financeProduct
   })

(s/def DMBusinessApplicant
  {:businessId :- s/Str
   :establishDate :- s/Date
   :numYearsInBusiness :- s/Int
   :legalName :- s/Str
   :contactPhoneNumber :- s/Str
   :contactEmail :- s/Str
   :phoneNumber :- s/Str
   :doingBusinessAsName :- s/Str
   :companyName :- s/Str
   :companyRegistrationNumber :- s/Str
   :companyPostCode :- s/Str
   :validSMEBureau  :- s/Str
   :bankTimeMonths  :- Int
   :bankTimeYears   :- Int
   :sortCode        :- s/Str
   :accountHolder   :- s/Str
   :isNewBureauRequired
   :accountNumber
   :paymentMethod
   :VATNumber
   :natureOfBusiness
   :businessStartDate
   :businessAddress {}
   :commercialBureau :- [DMCommercialBureau]
   :policyAndDLA :- {:dynamicRuleDetail :- DMDynamicRuleDetail}
   :complianceCriteria :- {:dynamicRuleDetail :- DMDynamicRuleDetail}
   :fraudCriteria  :- {:dynamicRuleDetail :- DMDynamicRuleDetail} 
   :Summary {}
   :applicantBankDetail {}})

(s/def DMCalcuatedValues 
  {:vehicleValuation
   :convertedMileage
   :previousAppVersion
   :aggregateExposure
   :aggregatePotentialExposure
   :prevApprovalReassess
   :preDecisionDocsRequired
   :prevConditionalApprovalReassess
   :trueCommercial
   :lowerLevelCLFlag
   :hasValidBureau
   :missingValuation
   :GBPtoUSDExch
   :derivedValuation
   })

(s/def DMDecisionResponse 
  [{:Product {:Decision [{:DecisionResult :- s/Str
                          :DecisionStatusIndicator :- s/Str}]}}
   ])

(s/def DMCreditApplication 
  {:id
   :DFEId
   :applicationNumber
   :type
   :versionNumber
   :receivedDate
   :expirationTimestamp
   :enteredTimestamp
   :productCategoryName
   :isVersionActive
   :countryCode
   :dealerName
   :dealerGroup 
   :GMFIsBureauLock
   :GMFBureauLockDate
   :TotalAmountFinanced
   :originalApplicationDate
   :ApplicationExceptionFlag
   :installment
   :vehicleAgeContractEnd
   :dmCallSequence
   :calculatedValues :- DMCalculatedValues
   :personalApplicant :- [DMPersonalApplicant]
   :business  :- DMBusinessApplicant
   :applicationAsset {:vehicleAge}
   :summaryList {:Summary [{:label
                            :value
                            :orderNo}]}
p   :summaryFlag [{key value}]
   })

(s/def DMApplication
  {:ApplicationDate :- s/Date
   :DeliveryOptionCode :- s/Str
   :ProcessingRequestType :- s/Str
   :ResubmissionFlag  :- s/Bool
   :StrategySelectionRandomNumber :- s/Int
   :Timestamp
   :TransactionId
   :CreditRequest {:DMFunction   
                   :DMProduct    
                   :ProductCategory 
                   :ProductCode     
                   :autoCollateral {:vehicleAsset {:type :- s/Str}}
                   :dealerGroup :- s/Str 
                   :assetType :- s/Str  
                   :financialDetails :- DMFinancialDetails}
   :DecisionResponse :- DMDecisionResponse
   :creditApplication :- DMCreditApplication}
