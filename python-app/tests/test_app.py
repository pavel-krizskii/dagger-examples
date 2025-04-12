import unittest
import time
from app import greetings

class TestApp(unittest.TestCase):

    def test_positive_1(self):
        time.sleep(3)
        self.assertEqual(greetings("Username"), "Hello, Username")

    def test_positive_2(self):
        time.sleep(3)
        pass

    def test_negative_1(self):
        time.sleep(3)
        self.fail("I shall not pass")

    def test_negative_2(self):
        time.sleep(3)
        self.fail("Failure is my goal")

    @unittest.skip
    def test_to_skip(self):
        print("You'll never see me")

if __name__ == "__main__":
    unittest.main()