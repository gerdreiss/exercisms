module LeapYear (isLeapYear) where

-- on every year that is evenly divisible by 4
--  except every year that is evenly divisible by 100
--   unless the year is also evenly divisible by 400
isLeapYear :: Integer -> Bool
isLeapYear year
  | year `mod` 400 == 0 = True
  | year `mod` 100 == 0 = False
  | year `mod` 4 == 0   = True
  | otherwise           = False
