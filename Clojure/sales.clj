(def cust (slurp "cust.txt"))
(def prod (slurp "prod.txt"))
(def sales (slurp "sales.txt"))

;;atoms variables
(def z (atom 1))
(def flag (atom 0))
(def option (atom " "))
(def entername(atom " "))
(def total_prod_value(atom 0))
(def prod_value(atom 0))
(def enter_product (atom " "))
(def sale_count (atom 0))
(def i (atom 0))
(def j (atom 0))
(def k (atom 0))
(def ii(atom 0))
(def temp (atom " "))
(def temp1 (atom " "))
(def temp2 (atom " "))
(def n4 (atom 0))
(def n5 (atom 0))

(def cust1 (clojure.string/split-lines cust))
(def no_of_customers (count cust1))
(def cust_data(atom[]))
(reset! i 0)
(while (< @i no_of_customers)
	(do
		(reset! temp (get cust1 @i))
		(def cust2 (clojure.string/split @temp #"\|"))
		(swap! cust_data conj cust2)
		(swap! i inc)
	)
)

(def cust_idlist0 (atom []))
(def cust_data0 (atom []))
(reset! i 0)
(while (< @i no_of_customers)
	(do
		(swap! cust_idlist0 conj (java.lang.Integer/parseInt (get-in @cust_data [@i 0])))
		(swap! i inc)
		))
(def cust_idlist (into [] (sort < @cust_idlist0)))
(reset! i 0)


(while (< @i no_of_customers)
(do
	(reset! j 0)
	(while (< @j no_of_customers)
		(do
			(if (= (get cust_idlist @i) (java.lang.Integer/parseInt (get-in @cust_data [@j 0])))
				(do
					(swap! cust_data0 conj (into[] (get @cust_data @j)))
				)
			)
		(swap! j inc)
		)
	)
	(swap! i inc)
))
(reset! i 0)
(reset! j 0)
(def sorted_cust_data (into [] @cust_data0))
;sorted_cust_data


(def prod1 (clojure.string/split-lines prod))
(def no_of_products (count prod1))
(def prod_data(atom[]))
(reset! j 0)
(while (< @j no_of_products)
	(do
		(reset! temp1 (get prod1 @j))
		(def prod2 (clojure.string/split @temp1 #"\|"))
		(swap! prod_data conj prod2)
		(swap! j inc)
	)
)
(reset! j 0)
(def prod_idlist0 (atom []))
(def prod_data0 (atom []))
(reset! i 0)
(while (< @i no_of_products)
	(do
		(swap! prod_idlist0 conj (java.lang.Integer/parseInt (get-in @prod_data [@i 0])))
		(swap! i inc)
		))
(def prod_idlist (into [] (sort < @prod_idlist0)))


(reset! i 0)
(while (< @i no_of_products)
(do
	(reset! j 0)
	(while (< @j no_of_products)
		(do
			(if (= (get prod_idlist @i) (java.lang.Integer/parseInt (get-in @prod_data [@j 0])))
				(do
					(swap! prod_data0 conj (into[] (get @prod_data @j)))
				)
			)
		(swap! j inc)
		)
	)
	(swap! i inc)
))
(reset! i 0)
(reset! j 0)
(def sorted_prod_data (into [] @prod_data0))
;sorted_prod_data


(def sales1(clojure.string/split-lines sales))
(def no_of_sales (count sales1))
(def sales_data(atom[]))
(reset! k 0)
(while (< @k no_of_sales)
	(do
		(reset! temp2 (get sales1 @k))
		(def sales2 (clojure.string/split @temp2 #"\|"))
		(swap! sales_data conj sales2)
		(swap! k inc)
	)
)
(reset! k 0)

(def sales_idlist0 (atom []))
(def sales_data0 (atom []))
(reset! i 0)
(while (< @i no_of_sales)
	(do
		(swap! sales_idlist0 conj (java.lang.Integer/parseInt (get-in @sales_data [@i 0])))
		(swap! i inc)
		))
(def sales_idlist (into [] (sort < @sales_idlist0)))
(reset! i 0)


(while (< @i no_of_sales)
(do
	(reset! j 0)
	(while (< @j no_of_sales)
		(do
			(if (= (get sales_idlist @i) (java.lang.Integer/parseInt (get-in @sales_data [@j 0])))
				(do
					(swap! sales_data0 conj (into[] (get @sales_data @j)))
				)
			)
		(swap! j inc)
		)
	)
	(swap! i inc)
))
(reset! i 0)
(reset! j 0)
(def sorted_sales_data (into [] @sales_data0))
;sorted_sales_data

;; Functions
(defn fetch_cust_name[cid]
	(reset! k 0)
	(while (< @k no_of_customers )
		(do
			(if (= cid (get-in sorted_cust_data [@k 0]))
				(do
					(print (get-in sorted_cust_data [@k 1]))
				)
			)
			(swap! k inc)
		)
	)
	(reset! k 0)
)

(defn fetch_prod_desc [pid]
	(reset! k 0)
	(while (< @k no_of_products )
		(do
			(if (= pid (get-in sorted_prod_data [@k 0]))
				(do
					(print (get-in sorted_prod_data [@k 1]))
				)
			)
			(swap! k inc)
		)
	)
	(reset! k 0)
)

(defn fetch_prod_id [pname]
	(reset! n5 0)
	(reset! k 0)
	(reset! ii 0)
	(reset! sale_count 0)
	(while (< @k no_of_products)
		(do
			(if (=  pname (get-in sorted_prod_data [@k 1]))
				(do
					(reset! n5 1)
					(print (get-in sorted_prod_data [@k 1]) ": ")
					(while (< @ii no_of_sales)
						(do
							(if (=  (get-in sorted_prod_data [@k 0]) (get-in sorted_sales_data [@ii 2]))
								(do
									(reset! sale_count (+ @sale_count (java.lang.Integer/parseInt (get-in sorted_sales_data [@ii 3])) ) )

								)
							)
							(swap! ii inc)	
						)
					)
					(println @sale_count)
				)
			)
			(swap! k inc)
		)
	)
	(reset! k 0)
	(reset! ii 0)
	(reset! sale_count 0)
	(if (= @n5 0) (println "Product not found"))

)




(defn total_val [ccid]
	(reset! k 0)
	(reset! total_prod_value 0)
	(reset! prod_value 0)
	(while (< @k no_of_sales)
		(do
			(if (= ccid (get-in sorted_sales_data [@k 1]))
				(do
					(reset! j 0)
					(while (< @j no_of_products )
						(do
							(if (= (get-in sorted_sales_data [@k 2]) (get-in sorted_prod_data [@j 0]))
								(do
									(reset! prod_value (* (java.lang.Double/parseDouble (get-in sorted_prod_data [@j 2])) (java.lang.Double/parseDouble (get-in sorted_sales_data [@k 3]))))
									(reset! total_prod_value (+ @total_prod_value @prod_value))
								)
							)
							(swap! j inc)
						)
					)

				)
			)
			(swap! k inc)
		)
	)
	(fetch_cust_name ccid)
	(println  ": $" @total_prod_value)
	(reset! j 0)
	(reset! k 0)
	(reset! total_prod_value 0)
	(reset! prod_value 0)
)




(defn main[]
	(println " ")
	(println "*** Sales Menu ***")
	(println "-------------------")
	(println " ")
	(println "1. Display Customer Table")
	(println "2. Display Product Table")
	(println "3. Display Sales Table")
	(println "4. Total Sales for Customer")
	(println "5. Total Count for Product")
	(println "6. Exit")
	(println " ")
	(println "Enter an Option?")
	(reset! option (read-line))
	(case @option
		"1" (reset! flag 1)
		"2" (reset! flag 2)
		"3" (reset! flag 3)
		"4" (reset! flag 4)
		"5" (reset! flag 5)
		"6" (reset! flag 6)
		(reset! flag 0)
	)
)

(while (= @z 1)
	(do
		(main)



(if (= @flag 0)
	(do
		(println "Invalid Option, Try Again")
	)
)

(if (= @flag 1)
	(do
		(reset! i 0)
		(while (< @i no_of_customers)
			(do 
				(println (get-in sorted_cust_data [@i 0]) ": ["(get-in sorted_cust_data [@i 1]) "," (get-in sorted_cust_data [@i 2]) "," (get-in sorted_cust_data [@i 3]) "]")
				(swap! i inc)
			)
		)
		(reset! i 0)
	)
)

(if (= @flag 2)
	(do
		(reset! i 0)
		(while (< @i no_of_products)
			(do 
				(println (get-in sorted_prod_data [@i 0]) ": ["(get-in sorted_prod_data [@i 1]) "," (get-in sorted_prod_data [@i 2]) "]")
				(swap! i inc)
			)
		)
		(reset! i 0)
	)
)


(if (= @flag 3)
	(do 
		(reset! i 0)
		(while (< @i no_of_sales)
			(do 
				(println " ")
				(print (get-in sorted_sales_data [@i 0]) ": [")
				(fetch_cust_name (get-in sorted_sales_data [@i 1]))
				(print ",")
				(fetch_prod_desc (get-in sorted_sales_data [@i 2]))
				(print ",")
				(print (get-in sorted_sales_data [@i 3]))
				(print "]")
				(swap! i inc)
			)
		)
		(reset! i 0)
	)
)



(if (= @flag 4)
	(do 
		(reset! n4 0)
		(println "Enter Name:")
		(reset! entername (read-line))
		(reset! i 0)
		(while (< @i no_of_customers)
			(do
				
				(if (= @entername (get-in sorted_cust_data [@i 1]))
					(do 
						(reset! n4 1)
						(total_val (get-in sorted_cust_data [@i 0]))
					)

				)
				(swap! i inc)
			)
				
		)
		(if (= @n4 0) (println "Customer not found"))
		(reset! i 0)
	)
)


(if (= @flag 5)
	(do 
		(println "Enter Product:")
		(reset! enter_product (read-line))
		(fetch_prod_id @enter_product)
	)
)

(if (= @flag 6)
	(do 
		(println "Good Bye")
		(reset! z 0)
	)
)

))
