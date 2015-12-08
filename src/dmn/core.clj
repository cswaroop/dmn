(ns dmn.core
  (:require [clara.rules :refer :all]
            [dmn.domain :refer :all]))

(defn dt-common-pre-bureau-refer-rules 
  [prevAppVersion 
   prevAppStatus 
   applicationDetailsChanged
   prevApprovalReassess
   prevCondApprovalReassess
   preDecisionDocsRequired]
  (cond (and (= prevAppVersion "Y") 
             (= prevAppStatus "Approval") 
             (= applicationDetailsChanged "Y")) 
        (rs-common-pre-bureau-refer-rules)
        (and (= prevAppVersion "Y")
             (= prevAppStatus "Approval")
             (or (= applicationDetailsChanged "N") 
                 (= prevApprovalReassess "Y")))
        (rs-common-pre-bureau-refer-rules)))

(defn rs-common-pre-bureau-refer-rules 
  [preDecisionDocsRequired trueCommercial]
  (if (or (= prevDecisionDocsRequired "Yes") (= trueCommercial "Y"))
    (:investigate)))

;
;                scorecard
;

(defn chars {"LSC547" "M" ;Equifax: 1st App, Current Address, # months of arrears last 24 months
             "SSC5" "M"  ;Equifax: 1st App, Current Address, Total # searches
             "ESC14" "C" ;Equifax: 1st App, Current Address, Length of Residence
             "BSC464" "G" ;Equifax: 1st App, Current Address, All credit cards Max Bal / Curr Limit (live accounts)
             "LSC558"  "U" ;Equifax: 1st Applicant, Current Address, Worst status last 12 months (all accounts)
             "XPCF09"  "C" ;Equifax: 1st Applicant, Current Address, % of postcode houses with default
             "SSC013"  "M" ;Equifax: 1st App, Current Address, Min # months since credit search last 12 months
             "LSP572"  "S" ;Equifax: 1st App, Previous Address, Worst status last 12 months (all accounts)
             "SSP005"  "M" ;Equifax: 1st App, Previous Address, Total # searches
             "FSC128"  "M" ;Equifax: 1st App, Current Address, Fixed term accounts, # months since 1+
             "co_LSC568" "S" ;Equifax: 2nd App, Current Address, Worst status last 12 months (settled accounts)
             "DownPaymenttoCSP" "0" ;Downpayment to Cost selling Price %
             })

(defn 327-personal-leasing-scorecard [characteristics]
  (let [lsc547 ("LSC547" characterstics)]))

;
;                 decision tables
;

(defn dti-common-risk-tiers [score]
  (cond (< score 481) "E"
        (and (>= score 482) (< score 521)) "D"
        ()))
;
;               data  methods
; 

(defn dm-odds-calcs [score]
  (exp (- (* score 0.01718) 7.17124)))

(defn dm-bureau-age-parameter [] 5)
(defn dm-bureau-validity-period [] 90)
(defn dm-gbp-to-usd-exch [] 1.5653)

(s/defn dm-is-valid-bureau?  :- s/Bool
  [DMPersonalApplicant DMPreviousBureau]
  true)

(s/defn dm-is-primary-bureau? :- s/Bool
  [hasValidBureau
   manualBureauPull
   bureauName]
 )


(s/defn dm-has-true-commercial-exposure :- s/Bool 
  [creditLineLimit totalPotentialAggregateExposureUSD]
  (if  (or (>= creditLineLimit 250000) 
           (>= totalPotentialAggregateExposureUSD 250000))
    true
    false))

(s/defn dm-is-true-commercial-dealer  :- s/Bool
  [companyName dealerName]
  (if (= companyName dealerName)
    true
    false))

(s/defn dm-is-true-commercial :- s/Bool
  [trueCommercialExpsoure trueCommercialDealer]
  (if (and trueCommercialExposure trueCommercialDealer)
    true
    false)
  )
(defn dm-lower-level-cl-flag  :- s/Bool
  [creditLineFlag 
   creditLineLimit 
   totalPotentialAggregateExposureUSD 
   numberOfPotentialAssets]
  (if (or (and creditLineFlag (< creditLineLimit 250000))
          (< 65000 totalPotentialAggregateExposureUSD)
          (>= numberOfPotentialAssets 5))
    true
    false))

(defn dm-total-gmf-lending 
  [xs  :- [DMExperience]]
  (reduce + 0 ()))

(defn dm-value-of-os-applications [request])

(defn dm-aggregate-exposure :- s/Num
  [totalAmountFinanced totalGMFLending]
  (+ totalAmountFinanced totalGMFLending))
(defn dm-aggregate-exposure-usd [])

(defn dm-aggregate-potential-exposure 
  "ValueofOSApplications(all except where ApplicationStatus in Expired, Rejected, Cancelled, or Not Taken Up)"
  [totalAmountFinanced
   totalGMFLending
   valueOfOSApplications]
  (+ totalAmountFinanced totalGMFLending valueOfOSApplications))

(defn dm-aggregate-potential-exposure-usd 
  [aggregatePotentialExposure GBPtoUSDExch]
  (* aggregatePotentialExposure GBPtoUSDExch))

(defn dm-applicant-age [request])
(defn dm-recent-application [ssc51]
  (or (!= ssc51 "M")
      (!= ssc51 "C")))
(defn dm-down-paymets-to-csp  :- s/Int
  "Calculation of Downpayment to Cost Selling Price Scorecard Characteristic"
  [product
   totalDeposit
   totalAmountFinanced
   cashSellingPrice]
  (if (or (= product "LEASING")
          (= product "PCP"))
    (* (/ totalDeposit totalAmontFinanced)
       100)
    (* (/ totalDeposit cashSellingPrice)
       100)))
(defn dm-score-card-error [request])
(defn dm-upper-reject-table [request])
(defn dm-vehicle-valuation [request])

(defn dm-vehicle-age :- s/Int
  "number of full months round down to nearest integer"
  [firstRegistrationDate originalApplicationDate]
  (- originalApplicationDate firstRegistrationDate))

(defn dm-vehicle-age-contract-end [request])
(defn dm-vehicle-mileage-contract-end [request])
(defn dm-edcpct [product systemVehicleValuation totalAmountFinanced])
(defn dm-dp-pct)
(defn dm-file-variation [request])
(defn dm-score-card-char-co-LSC568 [request])

(defn dm-dti  :- s/Num
"Calculation of Debt to Income Ratio"
  [monthlyPayment 
   monthlyHousing 
   monthlyDebt
   monthlySalary
   monthlyOtherIncome]
  (let [dti (* (/ (+ monthlyPayment monthlyHousing monthlyDebt)
                  (+ monthlySalary monthlyOtherIncome))
               100)]
    (if (> dti 99)
      99
      dti)))

(defn dm-pti
  "Calculation of Payment to Income Ratio"
  [monthlyRepayment grossAnnualIncome]
  (* (/ monthlyRepayment grossAnnualIncome)
     100))

(defn dm-ltv 
  "Calculation of Loan to Value Ratio"
  [totalAmountFinanced
   vehicleValuation]
  (* (/ totalAmountFinanced vehicleValuation)
     100))

(defn dm-trade-in-equity 
  "Calculation of the total down payment on the deal"
  [partExchange settlement]
  (- partExchange settlement))

(defn dm-total-down-payment 
  [tradeInEquity fda cashDeposit]
  (+ tradeInEquity fda cashDeposit))

(defn dm-previous-app-version 
  [applicationVersion]
  (if (> applicationVersion 1)
    true
    false))
(defn dm-application-details-changed [request])

(defn dm-previous-approval-reassessment  :- s/Bool
"Determines whether the application needs to be fully re-assessed or whether it can be auto approved."
  [totalAmountFinanced 
   prevTotalAmountFinanced
   monthlyRepayment    
   prevMonthlyRepayment]
  (if (and (<= totalAmountFinanced prevTotalAmountFinanced)
           (<= monthlyRepayment prevMonthlyRepayment)
           (>= totalAmountFinanced 1500))
    false
    true))
(defn dm-pre-decison-docs-required [request])
(defn dm-scorecard-error [request])
(defn dm-credit-policy-criteria-1 [request])
(defn dm-ind-pcp-new-standard-credit-policy-limits-1 [])
(defn dm-ind-pcp-used-standard-credit-policy-limits-1 [])
(defn dm-ind-pcp-used-arnoldclark-credit-policy-limits-1 [])
(defn dm-credit-policy-exceptions-2 [])
(defn dm-dla-role [])
(defn dm-auto-verification-requirements [])
(defn dm-ind-auto-conditional-approval-calcs [])
(defn dm-applicaiton-exception-flag)
(defn dm-application-summary-flags [])
(defn dm-application-risk-tier [])
(defn dm-application-score-odds [])
(defn dm-application-summary-fields [])
(defn dm-ind-auto-action-calcs [])
(defn dm-ind-new-documentation-checklist [])
(defn dm-bankruptcy-flag [csc13 csp13 cse13 csn13])
(defn dm-suspected-money-laundering-flag [cashDeposit]
  (if (> cashDeposit 10000)
    "YES"
    "NO"))
(defn dm-ofac-flag [hsc1])
(defn dm-hmt-flag [nsc1])
(defn dm-cifas-direct-flag [asc200])
(defn dm-cifas-indirect-flag [asc200])

(defn dm-dob-mismatch-flag :- s/Str
  [fsc2 fsc3]
  (cond (= fsc3 "Y") "YES"
        (and (clojure.set/subset? #{fsc3} #{"N" "A" "X"})
             (= fsc2 "Y")) "YES"
        (and (clojure.set/subset? #{fsc3} #{"N" "A" "X"})
             (clojure.set/subset? #{fsc2} #{"N" "A" "X"})) "N/A"
        (or (clojure.set/subset? #{fsc3} #{"S" "T" "B" "D" "E" "F" "G" "H"})
            (and (clojure.set/subset? #{fsc3} #{"N" "A" "X"})
                 (clojure.set/subset? #{fsc2} #{"S" "T" "B" "D" "E" "F" "G" "H"}))) "NO"))
(defn dm-vr-years-current-address [esc14]
  (cond (= esc14 nil) "NOT AVAILABLE"
        (clojure.set/subset? #{esc14} #{"M" "C"}) "NO MATCH"
        :default esc14))
(defn dm-vr-years-previous-address [])

(defn dm-fraud-rule-flag [cbscore scoreodds ltv assetType]
  (if (and  (< cbscore 400)
            (< scoreodds 12)
            (> ltv 95)
            (= (clojure.string/upper-case assetType) "USED"))
    "YES"
    "NO"))

(defn dm-undisclosed-address-flag [a_e101]
  (if (and (number? a_e101)
           (pos? a_e101))
    "YES"
    "NO"))

(defn dm-associate-adverse-credit-flag [lac556 lap556]
  (let [m #{"1" "2" "3" "4" "5" "6" "A" "B" "D" "I" "R"}]
        (if (or (clojure.set/subset? #{lac556} m)
                (clojure.set/subset? #{lap556} m))
          "YES"
          "NO")))
(defn dm-fraud-section [])
(defn dm-compliance-section [])
(defn dm-cash-advance-indicator [])

(defn dm-ccj-indicator [csc3 cscp3 cse3 csn3])
(defn dm-iva-indicator [csc51]
  (cond (and  (number? csc51)
              (>= csc51 1)) "YES"
        (or (= csc51 "M")
             (= csc51 "C")) "NO INFO"
             :else "NO"))

(defn dm-bankruptcy-indicator [csc50]
  (cond (and  (number? csc50)
              (>= csc50 1)) "YES"
        (or (= csc50 "M")
             (= csc50 "C")) "NO INFO"
             :else "NO"))



