import {Tree, type TreeProps} from 'antd';
import {DownOutlined} from '@ant-design/icons';
import {MockOrganization} from "./MockOrganization.ts";

const OrganizationTree = () => {
    const onSelect: TreeProps['onSelect'] = (selectedKeys, info) => {
        console.log('선택된 조직:', selectedKeys, info);
    };

    return (
        <Tree
                showIcon
                defaultExpandAll
                switcherIcon={<DownOutlined />}
                treeData={MockOrganization}
                onSelect={onSelect}
                virtual={false}
                blockNode
            />
    );
}

export default OrganizationTree