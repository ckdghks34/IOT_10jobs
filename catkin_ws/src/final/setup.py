from setuptools import setup

package_name = 'final'

setup(
    name=package_name,
    version='0.0.0',
    packages=[package_name],
    data_files=[
        ('share/ament_index/resource_index/packages',
            ['resource/' + package_name]),
        ('share/' + package_name, ['package.xml']),
    ],
    install_requires=['setuptools'],
    zip_safe=True,
    maintainer='1',
    maintainer_email='ssafy5552@gmail.com',
    description='TODO: Package description',
    license='TODO: License declaration',
    tests_require=['pytest'],
    entry_points={
        'console_scripts': [
            'run_map = final.run_map:main',
            'odom = final.odom:main',
            'a_star = final.a_star:main',
            'a_star_local_path = final.a_star_local_path:main',
            'wall_tracking = final.wall_tracking:main',
            'path_tracking = final.path_tracking:main',
            'load_map = final.load_map:main',
            'client = final.client:main',
            'path_pub = final.path_pub:main'
        ],
    },
)
