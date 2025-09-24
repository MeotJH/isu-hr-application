import {Button, Descriptions} from "antd";
import {EditOutlined} from "@ant-design/icons";


const BasicInfo = () => {
    return (
        <div style={{background: '#ffffff', padding: '16px'}}>
                {/* 인사정보 부분 */}
                <div style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'start',
                    marginTop: '16px',
                    marginBottom: '16px'
                }}>
                    <h3 style={{ margin: 0 }}>인사 정보</h3>
                    <Button type="text" icon={<EditOutlined />} size="small" />
                </div>

                <Descriptions
                    column={1}
                    size="small"
                    colon={false}
                    labelStyle={{
                        width: '100px',
                        color: '#666',
                        fontWeight: 'normal'
                    }}
                    contentStyle={{
                        color: '#333',
                        fontWeight: '500'
                    }}
                >
                    <Descriptions.Item label="사번">1102</Descriptions.Item>
                    <Descriptions.Item label="입사일">
                        2022. 2. 14
                        <span style={{ color: '#999', fontSize: '12px', marginLeft: '8px' }}>
                            재직기간 2022. 2. 14
                          </span>
                    </Descriptions.Item>
                    <Descriptions.Item label="조직 직책">리쿠르팅팀</Descriptions.Item>
                    <Descriptions.Item label="직무">Recruiting Manager</Descriptions.Item>
                    <Descriptions.Item label="직위">정보 미입력</Descriptions.Item>
                </Descriptions>

            {/* 조직정보 부분 */}
            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'start',
                marginTop: '16px',
                marginBottom: '16px'
            }}>
                <h3 style={{ margin: 0 }}>조직 정보</h3>
                <Button type="text" icon={<EditOutlined />} size="small" />
            </div>

            {/* 정보 목록 */}
            <Descriptions
                column={1}
                size="small"
                colon={false}
                labelStyle={{
                    width: '100px',
                    color: '#666',
                    fontWeight: 'normal'
                }}
                contentStyle={{
                    color: '#333',
                    fontWeight: '500'
                }}
            >
                <Descriptions.Item label="사번">1102</Descriptions.Item>
                <Descriptions.Item label="입사일">
                    2022. 2. 14
                    <span style={{ color: '#999', fontSize: '12px', marginLeft: '8px' }}>
                            재직기간 2022. 2. 14
                          </span>
                </Descriptions.Item>
                <Descriptions.Item label="조직 직책">리쿠르팅팀</Descriptions.Item>
                <Descriptions.Item label="직무">Recruiting Manager</Descriptions.Item>
                <Descriptions.Item label="직위">정보 미입력</Descriptions.Item>
            </Descriptions>
        </div>
    )
}

export default BasicInfo
