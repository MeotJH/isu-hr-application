# 인사관리 시스템 프론트엔드 개발 가이드

## 🏗️ 아키텍처 개요

이 프로젝트는 **React + TypeScript** 기반의 **관심사 분리 아키텍처**를 따릅니다.
MVVM 패턴과 유사하지만, React 생태계에 최적화된 구조입니다.

## 📋 핵심 개념

### 1. **상태관리 (State Management)**
**개념**: 앱 전체에서 공유되는 데이터(로그인 상태, 사용자 정보)를 관리
**라이브러리**: Zustand
**역할**: Redux보다 간단하고 보일러플레이트가 적은 상태관리

### 2. **라우팅 (Routing)**
**개념**: URL에 따라 다른 페이지를 보여주는 기능
**라이브러리**: React Router v6
**역할**: SPA에서 페이지 간 이동과 URL 관리

### 3. **서버 상태관리 (Server State)**
**개념**: API 호출, 캐싱, 로딩 상태를 자동으로 관리
**라이브러리**: TanStack Query (React Query)
**역할**: fetch/axios 대신 서버 데이터를 효율적으로 관리

### 4. **UI 컴포넌트**
**개념**: 재사용 가능한 디자인 시스템
**라이브러리**: Ant Design
**역할**: 일관된 UI/UX 제공

## 🔄 데이터 흐름 (Data Flow)

```
사용자 액션 → 컴포넌트 → Zustand Store → UI 업데이트
                    ↓
                API 호출 → TanStack Query → 서버 데이터 캐싱
```

## 📁 디렉토리 구조

```
src/
├── stores/           # 상태관리 (Zustand)
│   └── authStore.ts  # 로그인/사용자 상태
├── pages/            # 페이지 컴포넌트
│   ├── LoginPage.tsx
│   └── DashboardPage.tsx
├── components/       # 재사용 컴포넌트
├── types/           # TypeScript 타입 정의
│   └── auth.ts
├── hooks/           # React Query 커스텀 훅 (예정)
├── api/             # API 호출 함수들 (예정)
└── App.tsx          # 라우팅 설정
```

## 🚀 실제 구현 프로세스

### Step 1: 로그인 페이지 (LoginPage.tsx)
```tsx
// 1. Zustand store와 router 연결
const login = useAuthStore((state) => state.login)
const navigate = useNavigate()

// 2. 로그인 성공 시
login(userData)  // ← Zustand에 상태 저장
navigate('/dashboard')  // ← React Router로 페이지 이동
```

### Step 2: 라우팅 설정 (App.tsx)
```tsx
// 1. 보호된 라우트 설정
function ProtectedRoute({ children }) {
  const isAuthenticated = useAuthStore(state => state.isAuthenticated)
  return isAuthenticated ? children : <Navigate to="/login" />
}

// 2. 라우트 정의
<Routes>
  <Route path="/login" element={<LoginPage />} />
  <Route path="/dashboard" element={
    <ProtectedRoute><DashboardPage /></ProtectedRoute>
  } />
</Routes>
```

### Step 3: 대시보드 페이지 (DashboardPage.tsx)
```tsx
// 1. 로그인된 사용자 정보 가져오기
const { user, logout } = useAuthStore()

// 2. 로그아웃 처리
const handleLogout = () => {
  logout()  // ← Zustand에서 상태 제거
  navigate('/login')  // ← 로그인 페이지로 이동
}
```

## 🛠️ 개발 패턴

### 새로운 페이지 추가하기
1. `pages/` 폴더에 컴포넌트 생성
2. `App.tsx`에 라우트 추가
3. 필요시 Zustand store에 상태 추가

### API 연동하기 (예정)
1. `api/` 폴더에 API 함수 생성
2. `hooks/` 폴더에 TanStack Query 훅 생성
3. 컴포넌트에서 훅 사용

### 상태 추가하기
```tsx
// stores/someStore.ts
export const useSomeStore = create((set) => ({
  data: null,
  setData: (data) => set({ data }),
  clearData: () => set({ data: null })
}))
```

## 🔍 핵심 포인트

### ✅ 이렇게 하세요
- 클라이언트 상태는 Zustand 사용
- 서버 상태는 TanStack Query 사용
- 페이지 이동은 React Router 사용
- UI는 Ant Design 컴포넌트 활용

### ❌ 이렇게 하지 마세요
- useState로 전역 상태 관리 (prop drilling 발생)
- fetch/axios 직접 사용 (캐싱, 에러처리 복잡)
- window.location으로 페이지 이동 (SPA 장점 상실)

## 📚 참고 자료

- [Zustand 공식 문서](https://zustand-demo.pmnd.rs/)
- [React Router 공식 문서](https://reactrouter.com/)
- [TanStack Query 공식 문서](https://tanstack.com/query/)
- [Ant Design 공식 문서](https://ant.design/)

## 🎯 이 구조의 장점

1. **관심사 분리**: UI, 상태, 라우팅이 명확히 분리
2. **유지보수성**: 각 부분을 독립적으로 수정 가능
3. **확장성**: 새로운 기능 추가가 쉬움
4. **성능**: 필요한 부분만 리렌더링
5. **개발자 경험**: 직관적이고 예측 가능한 구조