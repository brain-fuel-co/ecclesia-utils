(ns org.ecclesiafoundation.clj.core.biblescraper.interface
  (:require [clj-http.client :as client]
            [clojure.pprint :refer pprint]
            [net.cgrand.enlive-html :as enlive]))

;;scratch pad
(require '[clj-http.client :as client]
         '[net.cgrand.enlive-html :as enlive]
         '[clojure.core.match :refer [match]])

(def url "https://ebible.org/kjv/PSA001.htm")

(defn get-html-from-url [url]
  (try
    (let [response (client/get url)]
      (:body response))
    (catch Exception e
      (println "An error occurred:" (.getMessage e)))))

(defn html->parsed-html [html-string]
  (enlive/html-resource (java.io.StringReader. html-string)))

(defn parsed-html->elements-with-class
  [parsed-html class-name & class-names]
  (let [keywords (vector (map #(keyword (str "." %)) class-name & class-names))]
    (enlive/select parsed-html keywords)))

(defn html->elements-with-class
  [html class-name & class-names]
  (let [parsed-html (html->parsed-html html)]
    (parsed-html->elements-with-class parsed-html class-name class-names)))

(defn extract-string [element]
  (match (type element)
         String element
         Map (extract-string (html->elements-with-class element "add" "nd"))
         Vector (map extract-string element)
         List (map extract-string element)
         _ nil))

  (let [html (get-html-from-url url)
        elements (html->elements-with-class html "q")]
    (->> elements
         clojure.pprint/pprint))
