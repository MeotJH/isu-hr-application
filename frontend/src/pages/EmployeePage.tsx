import { Typography, Card, Tabs, Row, Col } from 'antd'
import BasicInfo from "../components/employee/BasicInfo.tsx";
import EmployeeCard from "../components/employee/EmployeeCard.tsx";
import {useQuery} from "@tanstack/react-query";

const { Title } = Typography

const EmployeePage = () => {
    const items = [
        { key: '1', label: '기본', children: <BasicInfo/> },
        { key: '2', label: '탭 2', children: <div style={{background:'#f5f5f5',height:200,padding:16}}>탭 2 내용</div> },
        { key: '3', label: '탭 3', children: <div style={{background:'#f5f5f5',height:200,padding:16}}>탭 3 내용</div> },
    ];

    const employeeId = '10000' // URL params나 props에서 받아올 값

    const { data: employee, isLoading, error } = useQuery({
        queryKey: ['employee', employeeId],
        queryFn: async () => {
            console.log(`API 호출 시작: GET /employee/${employeeId}`)
            const response = await fetch(`http://localhost:8085/api/employee/${employeeId}`, {
                headers: { 'Content-Type': 'application/json' }
            })
            console.log('API 응답 상태:', response.status)
            const data = await response.json()
            console.log('API 응답 데이터:', data)
            return data
        }
    })

    console.log('React Query 상태:', { isLoading, error, employee })

    return (
        <div style={{ height:'100%', display:'flex', flexDirection:'column', overflow:'auto', flex: 1 }}>
            {/* 헤더 */}
            <Row gutter={16} style={{ flex:'0 0 auto', boxShadow:'0 4px 12px rgba(0,0,0,.15)' }}>
                <Col span={24} style={{ minWidth: 0 }}>
                    <Card style={{ borderRadius: 8 }}>
                        <Title level={4} style={{ margin: 0 }}>직원 관리</Title>
                    </Card>
                </Col>
            </Row>

            {/* 첫 번째 Card - 시간 섹션 */}
            <Row gutter={16} style={{ flex: '0 0 auto', marginTop: 10 }}>
                <Col span={24} style={{ minWidth: 0 }}>
                    <EmployeeCard/>
                </Col>
            </Row>

            {/* 두 번째 Card - Tabs */}
            <Row gutter={16} style={{ flex: '1 1 auto', marginTop: 10, minHeight: 0 }}>
                <Col span={24} style={{ minWidth: 0, display: 'flex' }}>
                    <Card
                        bordered={false}
                        style={{ width: '100%', boxShadow:'0 4px 12px rgba(0,0,0,.15)', marginBottom: 16 }}
                        bodyStyle={{ padding: 16 }}
                    >
                        <Tabs
                            defaultActiveKey="1"
                            items={items}
                            onChange={(key) => console.log('탭 변경:', key)}
                        />
                    </Card>
                </Col>
            </Row>
        </div>
    )
}

export default EmployeePage
