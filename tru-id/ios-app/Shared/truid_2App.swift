//
//  truid_2App.swift
//  Shared
//
//  Created by Anthony Belyaev on 09/09/2022.
//

import SwiftUI

@main
struct truid_2App: App {
    
    @StateObject private var authChecker = AuthChecker()
    
    var body: some Scene {
        WindowGroup {
            AuthView().environmentObject(authChecker)
        }
    }
}
