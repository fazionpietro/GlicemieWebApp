// main.tsx
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import Login from "./app/LoginPage/Login";
import { MantineProvider } from "@mantine/core";
import "@mantine/core/styles.css";
import Register from "./app/RegisterPage/Register";
import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import DashboardAdmin from "./app/DashboardAdmin/DashboardAdmin";
import UserPage from "./app/DashboardUser/UserPage";
import Unauthorized from "./routes/Unhautorized";
import { AuthProvider } from "./context/AuthContext";
import { ProtectedRoute } from "./routes/ProtectedRoute";
import DashboardMedico from './app/DashboardMedico/DashboardMedico.tsx'

import axios from "axios";
import { Assunzioni } from "./app/DashboardUser/Assunzioni.tsx";
axios.defaults.withCredentials = true;

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <MantineProvider defaultColorScheme="dark">
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            {/* Public routes that need AuthProvider */}
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/prova" element={<Assunzioni />} />
            <Route path="/unauthorized" element={<Unauthorized />} />

            {/* Private routes with ProtectedRoute */}
            <Route
              path="/admin"
              element={
                <ProtectedRoute requiredRole={["admin"]}>
                  <DashboardAdmin />
                </ProtectedRoute>
              }
            />
            <Route
              path="/medic"
              element={
                <ProtectedRoute requiredRole={["medico", "admin"]}>
                  <DashboardMedico />
                </ProtectedRoute>
              }
            />
            <Route
              path="/user"
              element={
                <ProtectedRoute requiredRole={["paziente", "medico", "admin"]}>
                  <UserPage />
                </ProtectedRoute>
              }
            />

            {/* Default redirects */}
            <Route path="/" element={<Navigate to="/login" replace />} />
            <Route path="*" element={<Navigate to="/login" replace />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </MantineProvider>
  </StrictMode>
);
