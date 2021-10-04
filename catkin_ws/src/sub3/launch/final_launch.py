from launch import LaunchDescription
from launch_ros.actions import Node

def generate_launch_description():
    return LaunchDescription([
        Node(
            package='ssafy_bridge',
            node_executable='udp_to_pub',
            node_name='udp_to_pub'
        ),
        Node(
            package='ssafy_bridge',
            node_executable='sub_to_udp',
            node_name='sub_to_udp'
        ),
        Node(
            package='ssafy_bridge',
            node_executable='udp_to_cam',
            node_name='udp_to_cam'
        ),
        Node(
            package='ssafy_bridge',
            node_executable='udp_to_laser',
            node_name='udp_to_laser'
        ),
        Node(
            package='sub3',
            node_executable='odom',
            node_name='odom',
            output='screen'
        ),
        Node(
            package='sub3',
            node_executable='a_star',
            node_name='a_star',
            output='screen'
        ),
        Node(
            package='sub3',
            node_executable='a_star_local_path',
            node_name='a_star_local_path',
            output='screen'
        ),
        Node(
            package='sub3',
            node_executable='map_client',
            node_name='map_client'
        ),
        Node(
            package='sub3',
            node_executable='run_map',
            node_name='run_map'
        ),
        # Node(
        #     package='sub3',
        #     node_executable='client',
        #     node_name='client'
        # ),
    ])