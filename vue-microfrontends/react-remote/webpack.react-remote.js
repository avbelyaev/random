const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
// const {ModuleFederationPlugin} = require('webpack').container;

// const deps = require('./package.json').dependencies;

module.exports = {
    entry: path.resolve(__dirname, './src/index.js'),
    mode: 'development',
    output: {
        publicPath: 'auto',
    },
    devServer: {
        host: '127.0.0.1',
        port: 5003,
    },
    optimization: {
        minimize: false,
    },
    resolve: {
        extensions: ['.jsx', '.js', '.json']
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: [
                            '@babel/preset-env',
                            ['@babel/preset-react', {"runtime": "automatic"}]
                        ]
                    }
                },
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.svg$/,
                use: [
                    {
                        loader: 'svg-url-loader',
                        options: {
                            limit: 10000,
                        },
                    },
                ],
            }
        ],
    },
    plugins: [
        // new ModuleFederationPlugin({
        //   name: 'remoteApp',
        //   filename: 'remoteEntry.js',
        //   exposes: {
        //     './Feed': './src/feed/Feed',
        //     './LoginsView': './src/logins/LoginsView',
        //   },
        //   remotes: {
        //     hostApp: 'hostApp@http://localhost:5001/remoteEntry.js'
        //   },
        //   shared: {
        //     vue: {
        //       singleton: true,
        //       eager: true
        //     },
        //   },
        // }),
        // new webpack.HotModuleReplacementPlugin(),
        // new MiniCssExtractPlugin({
        //     filename: '[name].css',
        // }),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, './src/index.html'),
        }),
    ]
}