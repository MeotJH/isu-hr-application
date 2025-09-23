import { useState } from 'react';
import {
  Form,
  Input,
  Button,
  Typography,
  Checkbox,
  App,
  Space
} from 'antd';
import { UserOutlined, LockOutlined, SafetyCertificateOutlined, TeamOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import type { LoginForm } from '../types/auth';

const { Title, Text } = Typography;

const LoginPage = () => {
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const { message } = App.useApp();
  const navigate = useNavigate();
  const login = useAuthStore((state) => state.login);

  const onFinish = async (values: LoginForm) => {
    setLoading(true);
    try {
      console.log('로그인 시도:', values);

      // TODO: 실제 API 호출로 대체
      await new Promise(resolve => setTimeout(resolve, 1500));
      if (values.username === 'admin' && values.password === 'admin123') {
        console.log('로그인 성공!:', values);

        // 가짜 사용자 데이터 생성
        const mockUser = {
          id: '1',
          username: values.username,
          name: '관리자',
          email: 'admin@company.com',
          department: 'IT부서',
          position: '시스템 관리자',
          role: 'admin' as const
        };

        // Zustand 스토어에 로그인 상태 저장
        login(mockUser);

        message.success('로그인 성공!');
        navigate('/dashboard');
      } else {
        message.error('아이디 또는 비밀번호가 올바르지 않습니다.');
      }
    } catch (error) {
      message.error('로그인 중 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      minHeight: '100vh',
      background: '#f0f2f5',
      display: 'flex'
    }}>
      {/* 좌측 브랜딩 영역 */}
      <div style={{
        flex: '1 1 60%',
        background: 'linear-gradient(135deg, #1890ff 0%, #722ed1 100%)',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        padding: '80px 60px',
        color: 'white',
        minWidth: '600px'
      }}>
        <Space direction="vertical" size="large" style={{ textAlign: 'center' }}>
          <TeamOutlined style={{ fontSize: '120px', color: 'white', opacity: 0.9 }} />

          <div>
            <Title level={1} style={{ color: 'white', fontSize: '48px', margin: 0 }}>
              인사 관리 시스템
            </Title>
            <Title level={3} style={{ color: 'white', opacity: 0.8, fontWeight: 300, margin: '16px 0' }}>
              Human Resources Management System
            </Title>
          </div>

          <div style={{ maxWidth: '500px', fontSize: '18px', opacity: 0.9, lineHeight: 1.6 }}>
            <p>효율적이고 체계적인 인사 관리를 위한 통합 솔루션</p>
            <p>• 직원 정보 관리 및 조직도</p>
            <p>• 근태 관리 및 휴가 시스템</p>
            <p>• 급여 및 평가 관리</p>
            <p>• 채용 프로세스 자동화</p>
          </div>
        </Space>
      </div>

      {/* 우측 로그인 폼 영역 */}
      <div style={{
        flex: '1 1 40%',
        background: 'white',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        padding: '80px 60px',
        minWidth: '500px'
      }}>
        <div style={{ maxWidth: '400px', width: '100%', margin: '0 auto' }}>
          {/* 로그인 헤더 */}
          <div style={{ textAlign: 'center', marginBottom: '48px' }}>
            <SafetyCertificateOutlined
              style={{
                fontSize: '56px',
                color: '#1890ff',
                marginBottom: '24px'
              }}
            />
            <Title level={2} style={{ margin: 0, color: '#262626' }}>
              시스템 로그인
            </Title>
            <Text type="secondary" style={{ fontSize: '16px' }}>
              계정 정보를 입력해 주세요
            </Text>
          </div>

          {/* 로그인 폼 */}
          <Form
            form={form}
            name="login"
            onFinish={onFinish}
            layout="vertical"
            size="large"
            autoComplete="off"
          >
            <Form.Item
              label={<span style={{ fontSize: '16px', fontWeight: 500 }}>사용자 ID</span>}
              name="username"
              rules={[
                { required: true, message: '사용자 ID를 입력해주세요!' },
                { min: 3, message: 'ID는 최소 3자 이상이어야 합니다.' }
              ]}
            >
              <Input
                prefix={<UserOutlined style={{ color: '#bfbfbf' }} />}
                placeholder="사용자 ID를 입력하세요"
                style={{
                  height: '56px',
                  fontSize: '16px',
                  borderRadius: '8px'
                }}
              />
            </Form.Item>

            <Form.Item
              label={<span style={{ fontSize: '16px', fontWeight: 500 }}>비밀번호</span>}
              name="password"
              rules={[
                { required: true, message: '비밀번호를 입력해주세요!' },
                { min: 6, message: '비밀번호는 최소 6자 이상이어야 합니다.' }
              ]}
            >
              <Input.Password
                prefix={<LockOutlined style={{ color: '#bfbfbf' }} />}
                placeholder="비밀번호를 입력하세요"
                style={{
                  height: '56px',
                  fontSize: '16px',
                  borderRadius: '8px'
                }}
              />
            </Form.Item>

            <Form.Item name="remember" valuePropName="checked" style={{ marginBottom: '32px' }}>
              <Checkbox style={{ fontSize: '15px' }}>로그인 상태 유지</Checkbox>
            </Form.Item>

            <Form.Item style={{ marginBottom: '24px' }}>
              <Button
                type="primary"
                htmlType="submit"
                loading={loading}
                style={{
                  width: '100%',
                  height: '56px',
                  fontSize: '18px',
                  fontWeight: '600',
                  borderRadius: '8px'
                }}
              >
                로그인
              </Button>
            </Form.Item>
          </Form>

          {/* 테스트 계정 안내 */}
          <div style={{
            backgroundColor: '#f6f8fa',
            border: '1px solid #e8e8e8',
            borderRadius: '8px',
            padding: '20px',
            marginTop: '32px'
          }}>
            <Text strong style={{ fontSize: '14px', color: '#595959' }}>
              개발/테스트 계정
            </Text>
            <br />
            <Text type="secondary" style={{ fontSize: '14px' }}>
              ID: admin / 비밀번호: admin123
            </Text>
          </div>

          {/* 푸터 */}
          <div style={{
            textAlign: 'center',
            marginTop: '40px',
            paddingTop: '24px',
            borderTop: '1px solid #f0f0f0'
          }}>
            <Text type="secondary" style={{ fontSize: '13px' }}>
              © 2024 HR Management System. All rights reserved.
            </Text>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;