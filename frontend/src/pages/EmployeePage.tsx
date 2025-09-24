import {Typography, Card, Layout, Tabs  } from 'antd'
import BasicInfo from "../components/employee/BasicInfo.tsx";
import EmployeeCard from "../components/employee/EmployeeCard.tsx";
const { Content } = Layout;

const { Title } = Typography

const EmployeePage = () => {


  const items = [
    {
      key: '1',
      label: '기본',
      children: <BasicInfo/>,
    },
    {
      key: '2',
      label: '탭 2',
      children: <div style={{background: '#f5f5f5', height: '200px', padding: '16px'}}>탭 2 내용</div>,
    },
    {
      key: '3',
      label: '탭 3',
      children: <div style={{background: '#f5f5f5', height: '200px', padding: '16px'}}>탭 3 내용</div>,
    },
  ];

  return (
    <div style={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <Content style={{
        padding: '6px',
        background: '#ffffff',
        borderRadius: '12px',
        flex: 1,
        overflow: 'auto'
      }}>
        {/* 페이지 헤더 */}
        <div style={{ marginBottom: '12px' }}>
          <div style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            marginBottom: '4px'
          }}>
            <Title level={5} style={{ margin: 0 }}>직원 관리</Title>
          </div>
        </div>

        {/* 첫 번째 Card - 시간 섹션 */}
        <EmployeeCard/>

        {/* 두 번째 Card - 3개 가로 박스 */}
        <Card style={{ marginBottom: 16, boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)', }} bordered={false}>
          <Tabs
              defaultActiveKey="1"
              items={items}
              onChange={(key) => console.log('탭 변경:', key)}
          />
        </Card>

      </Content>
    </div>
  )
}

export default EmployeePage