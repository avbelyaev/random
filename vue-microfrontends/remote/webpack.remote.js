const path = require('path');
const webpack = require('webpack');
const { VueLoaderPlugin } = require('vue-loader');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { ModuleFederationPlugin } = require('webpack').container;

// const deps = require('./package.json').dependencies;

module.exports = {
  entry: path.resolve(__dirname, './src/index.js'),
  mode: 'development',
  output: {
    publicPath: 'auto',
  },
  devServer: {
    host: '127.0.0.1',
    port: 5002,
  },
  optimization: {
    minimize: false,
  },
  resolve: {
    extensions: ['.vue', '.jsx', '.js', '.json']
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        loader: 'vue-loader'
      },
      {
        test: /\.css$/,
        use: [
          {
            loader: MiniCssExtractPlugin.loader,
            options: {},
          },
          'css-loader',
        ],
      },
    ],
  },
  plugins: [
    new ModuleFederationPlugin({
      name: 'remoteApp',
      filename: 'remoteEntry.js',
      exposes: {
        // './ExternalLoginsView': './src/views/ExternalLoginsView',
        './Feed': './src/feed/Feed',
      },
      shared: {
        // ...deps,
        vue: {
          singleton: true,
          // requiredVersion: deps.vue,
          // eager: true,
        },
      },
    }),
    new webpack.HotModuleReplacementPlugin(),
    new MiniCssExtractPlugin({
      filename: '[name].css',
    }),
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, './index.html'),
    }),
    new VueLoaderPlugin(),
  ]
}