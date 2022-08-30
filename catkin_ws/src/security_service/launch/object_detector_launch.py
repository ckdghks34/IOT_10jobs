from launch import LaunchDescription
from launch_ros.actions import Node

def generate_launch_description():
    return LaunchDescription([

        Node(
            package='security_service',
            node_executable='path_pub',
            node_name='path_pub'
        ),
        
        Node(
            package='security_service',
            node_executable='odom',
            node_name='odom'
        ),
        
        Node(
            package='security_service',
            node_executable='tf_detector',
            node_name='tf_detector'
        ),

        Node(
            package='security_service',
            node_executable='patrol_client',
            node_name='patrol_client'
        )
    ])



