#!/bin/bash -ex

# requires js dependencies and binds them to window object
npm run build

lein do clean, cljsbuild once min

rm -rf deployable
mkdir deployable
cp -r resources/public/* deployable