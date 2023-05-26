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
    port: 5001,
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
      name: 'hostApp',
      filename: 'remoteEntry.js',
      remotes: {
        host: 'hostApp@http://localhost:5001/remoteEntry.js',
        remoteApp: 'remoteApp@http://localhost:5002/remoteEntry.js',
      },
      exposes: {
        './store': './src/store'
      },
      shared: {
        // ...deps,
        vue: {
          singleton: true,
          // requiredVersion: deps.vue,
          eager: true,
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
