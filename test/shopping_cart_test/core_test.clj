(ns shopping-cart-test.core-test
  (:require [clojure.test :refer :all]
            [shopping-cart-test.core :refer :all]))

(defn test-checkout [expected-price items]
  (is (= (bigdec expected-price) (checkout items))))

(deftest checkout-test

  (testing "One apple should cost 0.60"
    (test-checkout 0.60 '(:apple)))

  (testing "One banana should cost 0.20"
    (test-checkout 0.20 '(:banana)))

  (testing "One orange should cost 0.25"
    (test-checkout 0.25 '(:orange)))

  (testing "Apples should be buy one get one free"
    (test-checkout 0.60 '(:apple :apple)))

  (testing "Bananas should be buy one get one free"
    (test-checkout 0.20 '(:banana :banana)))

  (testing "Bananas should be on the same offer as apples"
    (test-checkout 0.60 '(:banana :apple)))

  (testing "Oranges should be three for the price of two"
    (test-checkout 0.50 '(:orange :orange :orange)))

  (testing "apple apple orange apple"
    (test-checkout 1.45 '(:apple :apple :orange :apple)))

  (testing "One Melon costs 1.00"
    (test-checkout 1.00 '(:melon)))

  (testing "Melons should be buy two get one free"
    (test-checkout 2.00 '(:melon :melon :melon)))

  (testing "running total"
    (let [res (running-total-checkout '(:apple :apple :orange :apple))]
      (is (= (:totals res)
             [(bigdec 1.45) (bigdec 0.85) (bigdec 0.6) (bigdec 0.6)]))
      (is (= (:total res)
             (bigdec 1.45))))))
