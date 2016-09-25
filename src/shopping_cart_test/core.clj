(ns shopping-cart-test.core)

(def price-lookup {:apple  (bigdec 0.60)
                   :orange (bigdec 0.25)
                   :banana (bigdec 0.20)
                   :melon  (bigdec 1.00)})

(def item-to-offer {:apple  :buy-two-get-one-free
                    :banana :buy-two-get-one-free
                    :orange :buy-three-get-one-free
                    :melon  :melons-three-for-two})

(defn- buy-x-get-one-free [prices x]
  (let [number-to-remove (int (/ (count prices) x))
        items-after-offers-applied (drop number-to-remove (sort < prices))]
    (reduce + items-after-offers-applied)))

(def offer-to-fn {:buy-two-get-one-free   #(buy-x-get-one-free % 2)
                  :buy-three-get-one-free #(buy-x-get-one-free % 3)
                  :melons-three-for-two   #(buy-x-get-one-free % 3)})

(def offers (keys offer-to-fn))

(defn- calculate-price [offer items]
  (let [items-matching-offer (filter #(= (:offer % ) offer) items)
        prices (map #((:item %) price-lookup) items-matching-offer)
        offer-fn (offer offer-to-fn)]
    (offer-fn prices)))

(defn checkout [items]
  (let [items-with-offer (map (fn [x] {:item x :offer (item-to-offer x)}) items)
        prices (map #(calculate-price % items-with-offer) offers)]
    (reduce + prices)))

(defn- totals-calculator [checked-out-items, item-to-checkout]
  (let [items-with-new-item-added (conj (:items checked-out-items) item-to-checkout)
        total-so-far (checkout items-with-new-item-added)
        totals (conj (:totals checked-out-items) total-so-far)]
    {:items  items-with-new-item-added
     :totals totals
     :total  total-so-far
     }))

(defn running-total-checkout [items]
  (let [result (reduce totals-calculator '() items)]
    {:totals (:totals result)
     :total  (:total result)
     }))