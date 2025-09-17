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
import { AuthProvider } from "./context/AuthContext";
import { ProtectedRoute } from "./routes/ProtectedRoute";
import DashboardMedico from './app/DashboardMedico/DashboardMedico.tsx'

import axios from "axios";
import { Unhautorized } from "./routes/Unhautorized.tsx";
import { PageNotFound } from "./routes/PageNotFound.tsx";
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
            <Route path="/unauthorized" element={<Unhautorized />} />
            <Route path="/notfound" element={<PageNotFound />} />



            {/* Private routes with ProtectedRoute */}
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute requiredRole={["admin"]}>
                  <DashboardAdmin />
                </ProtectedRoute>
              }
            />
            <Route
              path="/dashdoard"
              element={
                <ProtectedRoute requiredRole={["medico"]}>
                  <DashboardMedico />
                </ProtectedRoute>
              }
            />
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute requiredRole={["paziente"]}>
                  <UserPage />
                </ProtectedRoute>
              }
            />

            {/* Default redirects */}
            <Route path="/" element={<Navigate to="/login" replace />} />
            <Route path="*" element={<Navigate to="/notfound" replace />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </MantineProvider>
  </StrictMode>
);
