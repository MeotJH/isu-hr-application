import { Button, Card, Col, Row, Typography, Space, Avatar } from 'antd'
import { UserOutlined, LogoutOutlined, TeamOutlined, CalendarOutlined, FileTextOutlined, SettingOutlined } from '@ant-design/icons'
import { useAuthStore } from '../stores/authStore'
import { useNavigate } from 'react-router-dom'

const { Title, Text } = Typography

const DashboardPage = () => {
  const { user, logout } = useAuthStore()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const menuItems = [
    {
      title: '직원 관리',
      description: '직원 정보 조회 및 관리',
      icon: <TeamOutlined style={{ fontSize: '32px', color: '#1890ff' }} />,
      path: '/employees'
    },
    {
      title: '조직도',
      description: '조직 구조 및 부서 관리',
      icon: <UserOutlined style={{ fontSize: '32px', color: '#52c41a' }} />,
      path: '/organization'
    },
    {
      title: '근태 관리',
      description: '출퇴근 및 휴가 관리',
      icon: <CalendarOutlined style={{ fontSize: '32px', color: '#fa8c16' }} />,
      path: '/attendance'
    },
    {
      title: '급여 관리',
      description: '급여 및 수당 관리',
      icon: <FileTextOutlined style={{ fontSize: '32px', color: '#eb2f96' }} />,
      path: '/payroll'
    }
  ]

  return (
    <div style={{ minHeight: '100vh', background: '#f0f2f5' }}>
      {/* 헤더 */}
      <div style={{
        background: '#fff',
        padding: '16px 24px',
        borderBottom: '1px solid #f0f0f0',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
          <TeamOutlined style={{ fontSize: '24px', color: '#1890ff' }} />
          <Title level={3} style={{ margin: 0 }}>인사 관리 시스템</Title>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
          <Space>
            <Avatar icon={<UserOutlined />} />
            <div>
              <Text strong>{user?.name || user?.username}</Text>
              <br />
              <Text type="secondary" style={{ fontSize: '12px' }}>{user?.department} · {user?.position}</Text>
            </div>
          </Space>
          <Button
            icon={<SettingOutlined />}
            type="text"
          />
          <Button
            icon={<LogoutOutlined />}
            type="text"
            onClick={handleLogout}
          >
            로그아웃
          </Button>
        </div>
      </div>

      {/* 메인 컨텐츠 */}
      <div style={{ padding: '24px' }}>
        <div style={{ marginBottom: '24px' }}>
          <Title level={2}>대시보드</Title>
          <Text type="secondary">인사 관리 시스템에 오신 것을 환영합니다.</Text>
        </div>

        {/* 메뉴 카드들 */}
        <Row gutter={[24, 24]}>
          {menuItems.map((item, index) => (
            <Col xs={24} sm={12} lg={6} key={index}>
              <Card
                hoverable
                style={{
                  height: '200px',
                  display: 'flex',
                  flexDirection: 'column',
                  justifyContent: 'center',
                  textAlign: 'center'
                }}
                onClick={() => {

                  navigate(item.path);
                  console.log(`Navigate to ${item.path}`)
                }}
              >
                <Space direction="vertical" size="large" style={{ width: '100%' }}>
                  {item.icon}
                  <div>
                    <Title level={4} style={{ margin: '8px 0' }}>{item.title}</Title>
                    <Text type="secondary">{item.description}</Text>
                  </div>
                </Space>
              </Card>
            </Col>
          ))}
        </Row>

        {/* 최근 활동 */}
        <Row gutter={[24, 24]} style={{ marginTop: '32px' }}>
          <Col span={24}>
            <Card title="최근 활동" style={{ minHeight: '200px' }}>
              <div style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                height: '150px',
                color: '#999'
              }}>
                <Text type="secondary">최근 활동 내역이 여기에 표시됩니다.</Text>
              </div>
            </Card>
          </Col>
        </Row>
      </div>
    </div>
  )
}

export default DashboardPage