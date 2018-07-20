(require 'cljs.build.api)

(cljs.build.api/build
  "src"
  {:main 'cljs-d3-mouse.core
   :output-to "target/main.js"
   :output-dir "target/main.out"
   :asset-path "main.out"
   :verbose true
   ;:debug true
   ;:optimizations :advanced
   :npm-deps {:d3-selection "latest"
              :d3-transition "latest"
              :d3-shape "latest"
              :d3-ease "latest"}
   :install-deps true})
