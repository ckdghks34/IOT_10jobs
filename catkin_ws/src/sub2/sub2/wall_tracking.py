import rclpy
from datetime import datetime
from rclpy.node import Node

from geometry_msgs.msg import Twist,Point, Point32
from ssafy_msgs.msg import TurtlebotStatus
from squaternion import Quaternion
from nav_msgs.msg import Odometry,Path
from sensor_msgs.msg import LaserScan, PointCloud
from math import pi,cos,sin,sqrt,atan2
import numpy as np

import math


regions_ = {
        'right': 0,
        'fright': 0,
        'front': 0,
        'fleft': 0,
        'left': 0,
    }
state_ = 0
state_dict_ = {
    0: 'find the wall',
    1: 'turn left',
    2: 'follow the wall',
}


class wallTracking(Node):

    def __init__(self) :
        super().__init__('wall_Tracking')
        self.cmd_pub = self. create_publisher(Twist,'cmd_vel',10)
        self.lidar_sub = self.create_subscription(LaserScan,'/scan',self.lidar_callback,10)
        self.subscription = self.create_subscription(Odometry,'/odom',self.odom_callback,10)
        self.status_sub = self.create_subscription(TurtlebotStatus,'/turtlebot_status',self.status_callback,10)
        self.automap_sub = self.create_subscription(Int8MultiArray,'/map_auto',self.mapauto_callback,10)

        self.cmd_msg = Twist()
        time_period = 0.05
        self.timer = self.create_timer(time_period,self.timer_callback)

        self.is_odom=False
        self.is_path=False
        self.is_status=False
        self.is_lidar = False

        # 장애물 충돌여부
        self.collision = False

        # 맵그리기 AutoMode 여부
        autoMode_state = False

    def timer_callback(self):
        
        if autoMode_state :
            print('state : ', state_)
            
            if state_ == 0:
            self.find_wall()
            
            elif state_ == 1:
            self.turn_right()
            
            elif state_ == 2:
            self.follow_the_wall()
            
            else:
                print('Unknown state!')
            
            self.cmd_pub.publish(self.cmd_msg)
        #print('timer Callback')

    def clbk_laser(self,msg):
        print("clbk_laser")
        global regions_
        # regions_ = {
        #     'right':  min(min(msg.ranges[0:143]), 10),
        #     'fright': min(min(msg.ranges[144:287]), 10),
        #     'front':  min(min(msg.ranges[288:431]), 10),
        #     'fleft':  min(min(msg.ranges[432:575]), 10),
        #     'left':   min(min(msg.ranges[576:713]), 10),
        # }

        # print('range(0 ~ 143) : ', msg.ranges[0:143])
        # print('range(144 ~ 287) : ', msg.ranges[144:287])
        # print('range(288 ~ 431) : ', msg.ranges[288:431])
        # print('range(432 ~ 575) : ', msg.ranges[432:575])
        # print('range(576 ~ 713) : ', msg.ranges[576:713])
        # regions_ = {
        #     'front' : min(min(msg.ranges[330:358]+msg.ranges[:30]),10),
        #     'fleft' : min(min(msg.ranges[30:60]),10),
        #     'left' : min(min(msg.ranges[60:120]),10),
        #     'fright' : min(min(msg.ranges[270:330]),10),
        #     'right' : min(min(msg.ranges[210:270]),10),
        # }

        regions_ = {
            'front' : min(min(msg.ranges[345:358]+msg.ranges[:15]),10),
            'fleft' : min(min(msg.ranges[15:60]),10),
            'left' : min(min(msg.ranges[60:120]),10),
            'fright' : min(min(msg.ranges[270:345]),10),
            'right' : min(min(msg.ranges[210:270]),10),
        }

        self.take_action()


    def change_state(self,state):
        print("change_state")

        global state_, state_dict_
        if state is not state_:
            print ('Wall follower - [%s] - %s' % (state, state_dict_[state]))
            state_ = state

    def take_action(self):
        print("take_action")
        global regions_
        regions = regions_

        self.cmd_msg.linear.x = 0.0
        self.cmd_msg.angular.z = 0.0
        
        self.state_description = ''
        
        d = 0.6
        
        print('regions[left] : ', regions['left'])
        print('regions[fleft] : ', regions['fleft'])
        print('regions[front] : ', regions['front'])
        print('regions[fright] : ', regions['fright'])
        print('regions[right] : ', regions['right'])

        
        if regions['front'] > d and regions['fright'] > d and regions['fleft'] > d:
            self.state_description = 'case 1 - nothing'
            self.change_state(0)
        elif regions['front'] < d and regions['fright'] > d and regions['fleft'] > d:
            self.state_description = 'case 2 - front'
            self.change_state(1)
        elif regions['front'] > d and regions['fright'] > d and regions['fleft'] < d:
            self.state_description = 'case 3 - fright'
            self.change_state(2)
        elif regions['front'] > d and regions['fright'] < d and regions['fleft'] > d:
            self.state_description = 'case 4 - fleft'
            self.change_state(0)
        elif regions['front'] < d and regions['fright'] > d and regions['fleft'] < d:
            self.state_description = 'case 5 - front and fright'
            self.change_state(1)
        elif regions['front'] < d and regions['fright'] < d and regions['fleft'] > d:
            self.state_description = 'case 6 - front and fleft'
            self.change_state(1)
        elif regions['front'] < d and regions['fright'] < d and regions['fleft'] < d:
            self.state_description = 'case 7 - front and fleft and fright'
            self.change_state(1)
        elif regions['front'] > d and regions['fright'] < d and regions['fleft'] < d:
            self.state_description = 'case 8 - fleft and fright'
            self.change_state(0)
        else:
            self.state_description = 'unknown case'
            print(regions)
        # if regions['front'] > d and regions['fleft'] > d:
        #     self.state_description = 'case 1 - nothing'
        #     self.change_state(0)
        # elif regions['front'] < d and regions['fleft'] > d:
        #     self.state_description = 'case 2 - front'
        #     self.change_state(1)
        # elif regions['front'] > d and regions['fleft'] < d:
        #     self.state_description = 'case 3 - fright'
        #     self.change_state(2)
        # elif regions['front'] > d and regions['fleft'] > d:
        #     self.state_description = 'case 4 - fleft'
        #     self.change_state(0)
        # elif regions['front'] < d and regions['fleft'] < d:
        #     self.state_description = 'case 5 - front and fright'
        #     self.change_state(1)
        # elif regions['front'] < d and regions['fleft'] > d:
        #     self.state_description = 'case 6 - front and fleft'
        #     self.change_state(1)
        # elif regions['front'] < d and regions['fleft'] < d:
        #     self.state_description = 'case 7 - front and fleft and fright'
        #     self.change_state(1)
        # elif regions['front'] > d and regions['fleft'] < d:
        #     self.state_description = 'case 8 - fleft and fright'
        #     self.change_state(0)
        # else:
        #     self.state_description = 'unknown case'
        #     print(regions)


    def find_wall(self):
        print("find_wall")
        self.cmd_msg.linear.x = 0.3
        self.cmd_msg.angular.z = -0.3

    def turn_right(self):
        print("turn_right")
        self.cmd_msg.angular.z = 0.4

    def follow_the_wall(self):
        print("follow_the_wall")
        self.cmd_msg.linear.x = 0.3

    def odom_callback(self, msg):

        self.is_odom=True
        self.odom_msg=msg
        '''
        로직 3. Quaternion 을 euler angle 로 변환
        ''' 
        q = Quaternion(msg.pose.pose.orientation.w, msg.pose.pose.orientation.x, msg.pose.pose.orientation.y, msg.pose.pose.orientation.z)
        _,_,self.robot_yaw = q.to_euler()

    def lidar_callback(self, msg):
        print('lidar_callback')
        # print('msg : ', msg)
        self.clbk_laser(msg)
        # self.lidar_msg = msg
        # if self.is_path == True and self.is_odom == True:

        #     pcd_msg = PointCloud()
        #     pcd_msg.header.frame_id = 'map'

        #     pose_x = self.odom_msg.pose.pose.position.x
        #     pose_y = self.odom_msg.pose.pose.position.y
        #     theta = self.robot_yaw
            
        #     t = np.array([
        #         [cos(theta), -sin(theta), pose_x],
        #         [sin(theta), cos(theta), pose_y],
        #         [0,0,1]
        #     ])           

        #     for angle, r in enumerate(msg.ranges) :
        #         global_point = Point32()

        #         # 로컬좌표를 map 기준좌표로 바꿈
        #         if 0.0 < r < 12:
        #             local_x = r * cos(angle * pi / 180)
        #             local_y = r * sin(angle * pi / 180)
        #             local_point = np.array([[local_x], [local_y], [1]])
        #             global_result = t.dot(local_point)
        #             global_point.x = global_result[0][0]
        #             global_point.y = global_result[1][0]
        #             pcd_msg.points.append(global_point)

        #     self.collision = False
        #     # 모든경로와 라이다간의 거리를 비교
        #     for waypoint in self.path_msg.poses:
        #         # for lidar_point in pcd_msg.points[:45] :
        #         #     distance = sqrt(pow(waypoint.pose.position.x - lidar_point.x, 2) + pow(waypoint.pose.position.y - lidar_point.y, 2))
        #         #     if distance < 0.05:
        #         #         self.collision = True
        #         #         # print('collision')
        #         # for lidar_point in pcd_msg.points[320:] :
        #         #     distance = sqrt(pow(waypoint.pose.position.x - lidar_point.x, 2) + pow(waypoint.pose.position.y - lidar_point.y, 2))
        #         #     if distance < 0.05:
        #         #         self.collision = True
        #                 # print('collision')
        #         for lidar_point in pcd_msg.points :
        #             distance = sqrt(pow(waypoint.pose.position.x - lidar_point.x, 2) + pow(waypoint.pose.position.y - lidar_point.y, 2))
        #             if distance < 0.05:
        #                 self.collision = True
            
        #     self.is_lidar = True
        
    def status_callback(self,msg):
        self.is_status=True
        self.status_msg=msg

    def mapauto_callback(self,msg):
        if autoMode_state == True and msg.data[0] == False :
            self.cmd_msg.linear.x = 0
            self.cmd_msg.angular.z = 0
            self.cmd_pub.publish(cmd_msg)

        self.autoMode_state = msg.data[0]
    
def main(args=None):

    rclpy.init(args=args)
    
    Wall_tracking = wallTracking()
    
    rclpy.spin(Wall_tracking)
    
    Wall_tracking.destroy_node()

    rclpy.shutdown()

if __name__ == '__main__':
    main()