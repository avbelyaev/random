//
//  ContentView.swift
//  Shared
//
//  Created by Anthony Belyaev on 09/09/2022.
//

import SwiftUI


struct AuthView: View {
    
    @EnvironmentObject var authChecker: AuthChecker
    
    @State private var name: String = ""
    @State private var number: String = ""
    @State private var authLogs: String = ""
    
    var body: some View {
        
        Text("Hello, \(name)")
            .font(.title)
        Text("Enter your name and number to login")
        
        
        
        VStack {
            TextField(
                "Given Name",
                text: $name
            )
            TextField(
                "Phone number",
                text: $number
            ).keyboardType(.numberPad)
        }.textFieldStyle(.roundedBorder)
        
        
        
        
        Button("Login", action: {
            print("button is pressed")
            Task {
                print("running the task")
                
                let authInitResult = await authChecker.init2FA(phoneNumber: "447840197888")
//                authLogs += "Check{id=\(authChecker.authInitResult.check_id), url=\(authChecker.authInitResult.check_url)}, "
                print("---")
                print(authInitResult)
                
                DispatchQueue.main.async {
                    authLogs = "res=\(authInitResult)"
                }
            }
        })
        
        
        
        Text(
            "Auth check progress: \(authLogs)"
        ).frame(width: 300)
        
    }
    
}

struct AuthView_Previews: PreviewProvider {
    static var previews: some View {
        AuthView()
    }
}
