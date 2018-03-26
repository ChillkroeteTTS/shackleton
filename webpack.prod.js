const webpack = require('webpack');
const merge = require('webpack-merge');
const path = require('path');

const BUILD_DIR = path.resolve(__dirname, 'resources', 'public', 'js');
const APP_DIR = path.resolve(__dirname, 'src', 'js');


const config = {
  entry: `${APP_DIR}/main.js`,
  output: {
    path: BUILD_DIR,
    filename: 'bundle.js'
  },
};

module.exports = merge(config,{
    plugins: [new webpack.optimize.UglifyJsPlugin({
                                                    compress: {
                                                                  warnings: false
                                                              }
                                                          }),
              new webpack.DefinePlugin({
                'process.env.NODE_ENV': JSON.stringify('production')
              })]
});