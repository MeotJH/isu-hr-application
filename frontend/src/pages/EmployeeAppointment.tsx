import { Typography, Card, Button, Table, Space, Tag } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons'

const { Title, Text } = Typography

const EmployeeAppointmentPage = () => {
    // 임시 데이터
    const mockEmployees = [
        {
            key: '1',
            id: 'EMP001',
            name: '김철수',
            department: 'IT부서',
            position: '개발팀장',
            email: 'kim@company.com',
            phone: '010-1234-5678',
            status: 'active'
        },
        {
            key: '2',
            id: 'EMP002',
            name: '이영희',
            department: '인사부',
            position: '인사담당',
            email: 'lee@company.com',
            phone: '010-2345-6789',
            status: 'active'
        },
        {
            key: '3',
            id: 'EMP003',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        },
        {
            key: '4',
            id: 'EMP004',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        },
        {
            key: '5',
            id: 'EMP005',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        }
        ,
        {
            key: '6',
            id: 'EMP006',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        }
        ,
        {
            key: '7',
            id: 'EMP007',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        }
        ,
        {
            key: '8',
            id: 'EMP008',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        },
        {
            key: '9',
            id: 'EMP009',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        }
        ,
        {
            key: '10',
            id: 'EMP010',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        }
        ,
        {
            key: '11',
            id: 'EMP011',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        }
        ,
        {
            key: '12',
            id: 'EMP012',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        }
        ,
        {
            key: '13',
            id: 'EMP013',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        }
        ,
        {
            key: '14',
            id: 'EMP014',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        }
        ,
        {
            key: '15',
            id: 'EMP015',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        }
        ,
        {
            key: '16',
            id: 'EMP016',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        },
        {
            key: '17',
            id: 'EMP017',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        }
        ,
        {
            key: '18',
            id: 'EMP018',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        }
        ,
        {
            key: '19',
            id: 'EMP019',
            name: '박민수',
            department: 'IT부서',
            position: '주니어 개발자',
            email: 'park@company.com',
            phone: '010-3456-7890',
            status: 'inactive'
        }

    ]

    const columns = [
        {
            title: '직원 ID',
            dataIndex: 'id',
            key: 'id',
            width: 100
        },
        {
            title: '이름',
            dataIndex: 'name',
            key: 'name',
            width: 120
        },
        {
            title: '부서',
            dataIndex: 'department',
            key: 'department',
            width: 120
        },
        {
            title: '직책',
            dataIndex: 'position',
            key: 'position',
            width: 150
        },
        {
            title: '이메일',
            dataIndex: 'email',
            key: 'email',
            width: 200
        },
        {
            title: '연락처',
            dataIndex: 'phone',
            key: 'phone',
            width: 150
        },
        {
            title: '상태',
            dataIndex: 'status',
            key: 'status',
            width: 100,
            render: (status: string) => (
                <Tag color={status === 'active' ? 'green' : 'red'}>
                    {status === 'active' ? '재직' : '퇴사'}
                </Tag>
            )
        },
        {
            title: '작업',
            key: 'action',
            width: 150,
            render: () => (
                <Space size="small">
                    <Button
                        type="text"
                        icon={<EditOutlined />}
                        size="small"
                    >
                        수정
                    </Button>
                    <Button
                        type="text"
                        icon={<DeleteOutlined />}
                        size="small"
                        danger
                    >
                        삭제
                    </Button>
                </Space>
            )
        }
    ]

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
                    <Title level={2} style={{ margin: 0 }}>직원 관리</Title>
                    <Button
                        type="primary"
                        icon={<PlusOutlined />}
                        onClick={() => console.log('새 직원 추가')}
                    >
                        새 직원 추가
                    </Button>
                </div>
                <Text type="secondary">직원 정보를 조회하고 관리할 수 있습니다.</Text>
            </div>

            {/* 통계 카드들 */}
            <div style={{ marginBottom: '24px' }}>
                <Space size="large" style={{ width: '100%' }}>
                    <Card size="small" style={{ minWidth: '150px' }}>
                        <div style={{ textAlign: 'center' }}>
                            <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#1890ff' }}>
                                {mockEmployees.filter(emp => emp.status === 'active').length}
                            </div>
                            <div style={{ color: '#666' }}>재직 중</div>
                        </div>
                    </Card>
                    <Card size="small" style={{ minWidth: '150px' }}>
                        <div style={{ textAlign: 'center' }}>
                            <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#52c41a' }}>
                                {mockEmployees.length}
                            </div>
                            <div style={{ color: '#666' }}>전체 직원</div>
                        </div>
                    </Card>
                    <Card size="small" style={{ minWidth: '150px' }}>
                        <div style={{ textAlign: 'center' }}>
                            <div style={{ fontSize: '24px', fontWeight: 'bold', color: '#fa541c' }}>
                                {new Set(mockEmployees.map(emp => emp.department)).size}
                            </div>
                            <div style={{ color: '#666' }}>부서 수</div>
                        </div>
                    </Card>
                </Space>
            </div>

            {/* 직원 테이블 */}
            <Card>
                <Table
                    columns={columns}
                    dataSource={mockEmployees}
                    pagination={{
                        total: mockEmployees.length,
                        pageSize: 10,
                        showSizeChanger: true,
                        showQuickJumper: true,
                        showTotal: (total, range) =>
                            `${range[0]}-${range[1]} / 총 ${total}명`
                    }}
                    scroll={{ x: 1200 }}
                />
            </Card>
        </div>
    )
}

export default EmployeeAppointmentPage