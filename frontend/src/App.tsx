import { App as AntdApp } from 'antd'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import LoginPage from './pages/LoginPage'
import DashboardPage from './pages/DashboardPage'
import MainLayout from './components/Layout/MainLayout'
import { useAuthStore } from './stores/authStore'
import EmployeeListPage from "./pages/EmployeeListPage.tsx";
import EmployeePage from "./pages/EmployeePage.tsx";

const queryClient = new QueryClient()

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated)
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" />
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AntdApp>
        <BrowserRouter>
          <Routes>
            {/* 로그인 페이지 (레이아웃 없음) */}
            <Route path="/login" element={<LoginPage />} />

            {/* 대시보드 (독립적인 레이아웃) */}
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <DashboardPage />
                </ProtectedRoute>
              }
            />

            {/* 사이드바가 있는 페이지들 (중첩 라우팅) */}
            <Route
              path="/*"
              element={
                <ProtectedRoute>
                  <MainLayout />
                </ProtectedRoute>
              }
            >
              <Route path="employees" element={<EmployeeListPage />} />
              <Route path="employee" element={<EmployeePage />} />
              {/* 향후 추가될 페이지들 */}
              <Route path="organization" element={<div>조직도 페이지</div>} />
              <Route path="attendance" element={<div>근태 관리 페이지</div>} />
              <Route path="payroll" element={<div>급여 관리 페이지</div>} />
              <Route path="settings" element={<div>설정 페이지</div>} />
            </Route>

            {/* 루트 경로 리다이렉트 */}
            <Route path="/" element={<Navigate to="/dashboard" />} />
          </Routes>
        </BrowserRouter>
      </AntdApp>
    </QueryClientProvider>
  )
}

export default App
