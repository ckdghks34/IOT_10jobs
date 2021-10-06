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
            package='final',
            node_executable='path_pub',
            node_name='path_pub'
        ),
        # Node(
        #     package='security_service',
        #     node_executable='object_detector_client',
        #     node_name='object_detector_client'
        # ),
        Node(
            package='final',
            node_executable='odom',
            node_name='odom',
        ),
        Node(
            package='final',
            node_executable='a_star',
            node_name='a_star',
        ),
        Node(
            package='final',
            node_executable='a_star_local_path',
            node_name='a_star_local_path',
        ),
        Node(
            package='final',
            node_executable='client',
            node_name='client'
        ),
        Node(
            package='final',
            node_executable='run_map',
            node_name='run_map'
        ),
        Node(
            package='final',
            node_executable='wall_tracking',
            node_name='wall_tracking'
        ),
        Node(
            package='final',
            node_executable='load_map',
            node_name='load_map',
            output='screen'
        ),
        Node(
            package='final',
            node_executable='path_tracking',
            node_name='path_tracking'
        ),
    ])