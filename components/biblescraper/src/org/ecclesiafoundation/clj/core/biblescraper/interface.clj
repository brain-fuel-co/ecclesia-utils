(ns org.ecclesiafoundation.clj.core.biblescraper.interface
  (:require [clj-http.client :as client]
            [clojure.pprint :refer pprint]
            [net.cgrand.enlive-html :as enlive]))

;;scratch pad
(require '[clj-http.client :as client]
         '[net.cgrand.enlive-html :as enlive]
         '[clojure.pprint :as pprint])

(def url "https://ebible.org/kjv/PSA001.htm")

(defn get-html-from-url [url]
  (try
    (let [response (client/get url)]
      (:body response))
    (catch Exception e
      (println "An error occurred:" (.getMessage e)))))

(defn html->nodes [html-string]
  (enlive/html-resource (java.io.StringReader. html-string)))

(defn nodes->nodes-with-class
  [nodes class-name]
  (let [selector (keyword (str "." class-name))]
    (enlive/select nodes [selector])))

(defn html->nodes-with-class
  [html class-name]
  (let [nodes (html->nodes html)]
    (nodes->nodes-with-class nodes class-name)))

(let [html (get-html-from-url url)
      nodes (html->nodes-with-class html "q")]
  (pprint/pprint nodes))
