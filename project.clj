(defproject shackleton "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.908"]
                 [reagent "0.7.0" :exclusions [cljsjs/react cljsjs/react-dom]]
                 [org.clojure/core.async "0.4.474"]
                 [re-frame "0.10.5"]]

  :plugins [[lein-cljsbuild "1.1.5"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs   ["resources/public/css"]
             :nrepl-port 7888}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.4"]
                   [com.cemerick/piggieback "0.2.1"]
                   [figwheel-sidecar "0.5.8"]]

    :plugins      [[lein-figwheel "0.5.13"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "shackleton.core/mount-root"}
     :compiler     {:main                 shackleton.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :foreign-libs         [{:file     "resources/public/js/bundle.js"
                                            :provides ["cljsjs.react" "cljsjs.react.dom" "webpack.bundle"]}]
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}
                    }}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            shackleton.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :foreign-libs [{:file "resources/public/js/bundle.js"
                                    :provides ["cljsjs.react" "cljsjs.react.dom" "webpack.bundle"]}]
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    ]}

  )
