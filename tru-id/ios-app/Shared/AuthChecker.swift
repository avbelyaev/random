//
//  SilentAuthenticator.swift
//  truid-2 (iOS)
//
//  Created by Anthony Belyaev on 09/09/2022.
//

import Foundation

struct AuthCheck: Codable {
    var check_id: String
    var check_url: String
}

@MainActor class AuthChecker: ObservableObject {
    
    let ngrokHost = "https://c419-176-221-178-161.ngrok.io"
    
    func init2FA(phoneNumber: String) async -> AuthCheck {

        let url = URL(string: "\(ngrokHost)/init")!
        var request = URLRequest(url: url)
        
        print("serializing request for number \(phoneNumber)")
        let payload: [String: String] = [
           "number": phoneNumber
        ]
        let payloadData = try? JSONSerialization.data(withJSONObject: payload, options: .prettyPrinted)
         
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.addValue("application/json", forHTTPHeaderField: "Accept")
         
        async let responseData = {
            let (data, response) = try await URLSession.shared.upload(for: request, from: payloadData!)
            
            if let httpResponse = response as? HTTPURLResponse {
                print("response: \(httpResponse.statusCode)")
            }
            
            let responseData = try JSONDecoder().decode(AuthCheck.self, from: data)
            print(responseData)
            
            return responseData
        }()
        
        
    }
    
}
