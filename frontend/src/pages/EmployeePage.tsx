import { Typography  } from 'antd'

const { Title, Text } = Typography

const EmployeePage = () => {



  return (
    <div>
      {/* 페이지 헤더 */}
      <div style={{ marginBottom: '24px' }}>
        <div style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          marginBottom: '8px'
        }}>
          <Title level={5} style={{ margin: 0 }}>직원 관리</Title>
        </div>
        <Text type="secondary">직원 정보를 조회하고 관리할 수 있습니다.</Text>
      </div>
    </div>
  )
}

export default EmployeePage