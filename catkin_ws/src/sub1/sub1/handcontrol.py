import rclpy
from rclpy.node import Node
import os, time
from ssafy_msgs.msg import TurtlebotStatus,HandControl
import threading

# Hand Control 노드는 시뮬레이터로부터 데이터를 수신해서 확인(출력)하고, 메세지를 송신해서 Hand Control기능을 사용해 보는 노드입니다. 
# 메시지를 받아서 Hand Control 기능을 사용할 수 있는 상태인지 확인하고, 제어 메시지를 보내 제어가 잘 되는지 확인해보세요. 
# 수신 데이터 : 터틀봇 상태 (/turtlebot_status)
# 송신 데이터 : Hand Control 제어 (/hand_control)


# 노드 로직 순서
# 1. publisher, subscriber 만들기
# 2. 사용자 메뉴 구성
# 3. Hand Control Status 출력
# 4. Hand Control - Preview
# 5. Hand Control - Pick up
# 6. Hand Control - Put down


class Handcontrol(Node):

    def __init__(self):
        super().__init__('hand_control')
                
        ## 로직 1. publisher, subscriber 만들기
        self.hand_control = self.create_publisher(HandControl, '/hand_control', 10)                
        self.turtlebot_status = self.create_subscription(TurtlebotStatus,'/turtlebot_status',self.turtlebot_status_cb,10)
        

        # self.timer = self.create_timer(1, self.timer_callback)
        
        ## 제어 메시지 변수 생성 
        self.hand_control_msg=HandControl()        


        self.turtlebot_status_msg = TurtlebotStatus()
        self.is_turtlebot_status = False

        self.thread_turtlebot_status = threading.Thread(target=self.timer_callback)
        self.thread_turtlebot_status.start()
        

    def timer_callback(self):
        while True:
            # 로직 2. 사용자 메뉴 구성
            print('Select Menu [0: status_check, 1: preview, 2:pick_up, 3:put_down')
            menu=input(">>")
            if menu=='0' :               
                self.hand_control_status()
            if menu=='1' :
                self.hand_control_preview()               
            if menu=='2' :
                self.hand_control_pick_up()   
            if menu=='3' :
                self.hand_control_put_down()


    def hand_control_status(self):
        '''
        로직 3. Hand Control Status 출력
        '''
        print(self.turtlebot_status_msg.can_use_hand)

    def hand_control_preview(self):
        '''
        로직 4. Hand Control - Preview(Object를 어디다 내려놓게 되는지 미리 보여주는 기능)
        '''
        # /hand_control 토픽의 control_mode에 1을 넣고 publish해주면 Preview모드로 바뀜
        # Object를 내려놓을 수 있다면 /turtlebot_status 토픽의 can_put이 true / 내려놓을 수 없다면 Object 색깔 빨간색으로
        # /hand_control 토픽의 put_distance, put_height는 오브젝트를 두는 장소(put_dist는 Object를 x축 방향 떨어진 거리, height는 z축)

        self.hand_control_msg.control_mode = 1
        self.hand_control_msg.put_distance = 1.0
        self.hand_control_msg.put_height = 1.0
        for i in range(500) :
            self.hand_control.publish(self.hand_control_msg)
        print(self.hand_control_msg.put_distance, self.hand_control_msg.put_height)
        
    def hand_control_pick_up(self):
        '''
        로직 5. Hand Control - Pick up(들어서)        
        '''
        # /hand_control 토픽의 control_mode에 2를 넣고 publish해주면 물체를 들 수 있다 -> can_lift(/turtlebot_status)가 true일 때
        if self.turtlebot_status_msg.can_lift == True :
            self.hand_control_msg.control_mode = 2
            while True :
                self.hand_control.publish(self.hand_control_msg)
                if self.turtlebot_status_msg.can_use_hand :
                    break
            self.hand_control_preview()
            print("Pick up!!")
        
    def hand_control_put_down(self):        
        '''
        로직 6. Hand Control - Put down(놔두기)
        '''
        # Preview 모드를 적용해 can_put이 True인 상태에서 놓기 가능
        # /hand_control 토픽의 control_mode에 3을 넣고 publish하면 물체를 내려놓을 수 있다
        if self.hand_control_msg.control_mode == 1:
            if self.turtlebot_status_msg.can_put == True:
                self.hand_control_msg.control_mode = 3
                
                while True :
                    self.hand_control.publish(self.hand_control_msg)
                    if not self.turtlebot_status_msg.can_use_hand :
                        break
                self.hand_control_preview()
                print("put down!!")

    def turtlebot_status_cb(self,msg):
        self.is_turtlebot_status=True
        self.turtlebot_status_msg=msg
        

def main(args=None):
    rclpy.init(args=args)
    sub1_hand_control = Handcontrol()    
    rclpy.spin(sub1_hand_control)
    sub1_hand_control.destroy_node()
    rclpy.shutdown()

if __name__ == '__main__':
    main()