#!/ C:\Python37\python.exe

import numpy as np
import cv2
import os
import rclpy
import socketio
import base64

from rclpy.node import Node
from sensor_msgs.msg import CompressedImage

sio = socketio.Client()
global auto_switch
auto_switch = 0
flag = False

@sio.event
def connect():
    print('connection established')

@sio.event
def disconnect():
    print('disconnected from server')

@sio.on('findWallet')
def findWallet(data):
    global auto_switch
    auto_switch = 1
    print('지갑찾기')

@sio.on('findBag')
def findBackpack(data):
    print('가방찾기')
    global auto_switch
    auto_switch = 2

@sio.on('findKey')
def findKey(data):
    print('키 찾기')
    global auto_switch
    auto_switch = 3

@sio.on('findRemote')
def findRemote(data):
    print('리모컨 찾기')
    global auto_switch
    auto_switch = 4

@sio.on('patrolOff')
def patrol_on():
    global auto_switch
    auto_switch = 0


def non_maximum_supression(bboxes, threshold=0.5):
    
    bboxes = sorted(bboxes, key=lambda detections: detections[3],
            reverse=True)
    new_bboxes=[]
    
    new_bboxes.append(bboxes[0])
    
    bboxes.pop(0)

    for _, bbox in enumerate(bboxes):

        for new_bbox in new_bboxes:

            x1_tl = bbox[0]
            x2_tl = new_bbox[0]
            x1_br = bbox[0] + bbox[2]
            x2_br = new_bbox[0] + new_bbox[2]
            y1_tl = bbox[1]
            y2_tl = new_bbox[1]
            y1_br = bbox[1] + bbox[3]
            y2_br = new_bbox[1] + new_bbox[3]
            
            x_overlap = max(0, min(x1_br, x2_br)-max(x1_tl, x2_tl))
            y_overlap = max(0, min(y1_br, y2_br)-max(y1_tl, y2_tl))
            overlap_area = x_overlap * y_overlap
            
            area_1 = bbox[2] * new_bbox[3]
            area_2 = new_bbox[2] * new_bbox[3]
            
            total_area = area_1 + area_2 - overlap_area

            overlap_area = overlap_area / float(total_area)

            if overlap_area < threshold:
                
                new_bboxes.append(bbox)

    return new_bboxes



class ObjectDetectorToServer(Node):

    def __init__(self):
        super().__init__('detect_to_server')

        self.subscription = self.create_subscription(
            CompressedImage,
            '/image_jpeg/compressed',
            self.img_callback,
            10)

        self.byte_data = None

        self.img_bgr = None
        
        self.timer_period = 0.03

        # self.img_saved = False

        # self.human_detected = False
        self.wal_detected = False
        self.key_detected = False
        self.bp_detected = False
        self.rc_detected = False

        self.dir_img = os.path.join("C:\\Users\\user\\Desktop\\catkin_ws\\src\\security_service\\web\\client", "detect.png")

        self.timer = self.create_timer(self.timer_period, self.timer_callback)

        # self.pedes_detector = cv2.HOGDescriptor()
        # self.pedes_detector.setSVMDetector(cv2.HOGDescriptor_getDefaultPeopleDetector())
        
        # sio.connect('http://127.0.0.1:12001')
        sio.connect('http://j5d201.p.ssafy.io:12001')
        cv2.imwrite(self.dir_img, np.zeros((240, 320, 3)).astype(np.uint8))


    def img_callback(self, msg):
    
        self.byte_data = msg.data

        np_arr = np.frombuffer(msg.data, np.uint8)

        self.img_bgr = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)
  

    def find_bbox(self):
        global flag

        # print(flag)
        """
        # 로직 3. bgr 이미지의 binarization
        # 지갑, 키 등의 물체에 대한 bgr 값을 알고, 이 값 범위에 해당되는
        # cv2.inRange 함수를 써서 각 물체에 대해 binarization 하십시오.
        """
        
        lower_wal = (100, 245, 255)
        upper_wal = (110, 255, 255)
        lower_bp = (100, 210, 235)
        upper_bp = (110, 220, 255)
        lower_rc = (100, 210, 200)
        upper_rc = (110, 220, 220)
        lower_key = (100, 240, 200)
        upper_key = (110, 250, 220)

        self.img_wal = cv2.inRange(self.img_bgr, lower_wal, upper_wal)

        self.img_bp = cv2.inRange(self.img_bgr, lower_bp, upper_bp)

        self.img_rc = cv2.inRange(self.img_bgr, lower_rc, upper_rc)

        self.img_key = cv2.inRange(self.img_bgr, lower_key, upper_key)


        """
        # 로직 4. 물체의 contour 찾기
        # 지갑, 키 등의 물체들이 차지한 픽셀만 흰색으로 이진화되어 있는 이미지에 대해서,
        # 흰색 영역을 감싸는 contour들을 구하십시오.
        # cv2.findContours를 가지고 
        """
        # cv2.findContours()를 이용하여 이진화 이미지에서 윤곽선(컨투어)를 검색합니다.
        # cv2.findContours(이진화 이미지, 검색 방법, 근사화 방법)을 의미합니다.
        # 반환값으로 윤곽선, 계층 구조를 반환합니다.
        contours_wal, _ = cv2.findContours(self.img_wal, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        contours_bp, _ = cv2.findContours(self.img_bp, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        contours_rc, _ = cv2.findContours(self.img_rc, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        contours_key, _ = cv2.findContours(self.img_key, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        if contours_wal :
            self.wal_detected = True
        else :
            self.wal_detected = False

        if contours_bp :
            self.bp_detected = True
        else :
            self.bp_detected = False

        if contours_rc :
            self.rc_detected = True
        else : 
            self.rc_detected = False
        
        if contours_key :
            self.key_detected = True
        else : 
            self.key_detected = False

        
        # 물건 찾기 실행했을 때
        if auto_switch:
            # 지갑 찾기
            if auto_switch == 1 and self.wal_detected and flag:
                self.byte_data = cv2.imencode('.jpg', self.img_bgr*255)[1].tobytes()
                b64data = base64.b64encode(self.byte_data)
                sio.emit('walletStreaming', b64data.decode( 'utf-8' ) )
            # 가방 찾기
            elif auto_switch == 2 and self.bp_detected and flag:
                # cv2.imwrite('../web/client/backpack.jpg', self.img_bgr)
                self.byte_data = cv2.imencode('.jpg', self.img_bgr*255)[1].tobytes()
                b64data = base64.b64encode(self.byte_data)
                sio.emit('backpackStreaming', b64data.decode( 'utf-8' ) )
            # 키 찾기
            elif auto_switch == 3 and self.key_detected and flag:
                # cv2.imwrite('../web/client/key.jpg', self.img_bgr)
                self.byte_data = cv2.imencode('.jpg', self.img_bgr*255)[1].tobytes()
                b64data = base64.b64encode(self.byte_data)
                sio.emit('keyStreaming', b64data.decode( 'utf-8' ) )
            # 리모콘 찾기
            elif auto_switch == 4 and self.rc_detected and flag:
                # cv2.imwrite('../web/client/wallet.jpg', self.img_bgr)
                self.byte_data = cv2.imencode('.jpg', self.img_bgr*255)[1].tobytes()
                b64data = base64.b64encode(self.byte_data)
                sio.emit('remoteStreaming', b64data.decode( 'utf-8' ) )



        # print(contours_bp)
        # if contours_bp:
        #     cv2.imwrite()

        """
        # 로직 5. 물체의 bounding box 좌표 찾기
        """
        
        self.find_cnt(contours_wal)
        
        self.find_cnt(contours_bp)
        
        self.find_cnt(contours_rc)
        
        self.find_cnt(contours_key)


    def find_cnt(self, contours):
        global flag
        # print("좌표가 있나?")

        """
        # 로직 5. 물체의 bounding box 좌표 찾기
        # 지갑, 키 등의 물체들의 흰색 영역을 감싸는 contour 결과를 가지고
        # bbox를 원본 이미지에 draw 하십시오.
        """ 
        
        for cnt in contours:
            cnt = cnt.reshape( len(cnt) ,2).T
            x, y, w, h = min(cnt[0]), min(cnt[1]), max(cnt[0]), max(cnt[1])
            # print(x)
            if x >=100 and x <= 500:
                flag = True
            else:
                flag = False
            # print(x)

            cv2.rectangle(self.img_bgr, (x, y), (w, h), (0,0,255), 1 )

        # countours들로 바로 그려줌
        # cv2.drawContours(self.img_bgr, contours, -1, (0,0,255), 3)

        
    def timer_callback(self):

        if self.img_bgr is not None:

            # print("subscribed")
            # print("지갑:",self.wal_detected)
            # print("백팩:",self.bp_detected)
            # print("리모컨:",self.rc_detected)
            # print("지갑:",self.wal_detected)
            

            self.find_bbox()

            b64data = base64.b64encode(self.byte_data)

            if self.wal_detected:

                self.byte_data = cv2.imencode('.jpg', self.img_bgr)[1].tobytes()
            
            if self.key_detected:

                self.byte_data = cv2.imencode('.jpg', self.img_bgr)[1].tobytes()
            
            if self.bp_detected:

                self.byte_data = cv2.imencode('.jpg', self.img_bgr)[1].tobytes()
            
            if self.rc_detected:

                self.byte_data = cv2.imencode('.jpg', self.img_bgr)[1].tobytes()

            sio.emit('streaming', b64data.decode( 'utf-8' ) )

            if self.wal_detected:

                str_to_web = "wallet detected"
            
            if self.key_detected:

                str_to_web = "key detected"
            
            if self.bp_detected:

                str_to_web = "backpack detected"
            
            if self.rc_detected:

                str_to_web = "remote control detected"

            else:

                str_to_web = "safe"

            sio.emit('safety_status', str_to_web)

        else:

            pass
        

def main(args=None):
    
    rclpy.init(args=args)

    object_detector = ObjectDetectorToServer()

    rclpy.spin(object_detector)

    object_detector.destroy_node()
    
    rclpy.shutdown()

    sio.disconnect()


if __name__ == '__main__':
    main()
