# -*- coding: utf-8 -*-


def sort(input_tuple):
	def sort_part(array, left_boundary, right_boundary):
		pivot = array[left_boundary + (right_boundary - left_boundary) // 2]
		left = left_boundary
		right = right_boundary
		while left < right:
			while array[left] < pivot:
				left += 1
			while array[right] > pivot:
				right -= 1
			if left <= right:
				temp = array[left]
				array[left] = array[right]
				array[right] = temp
				left += 1
				right -= 1
		if right > left_boundary:
			sort_part(array, left_boundary, right)
		if left < right_boundary:
			sort_part(array, left, right_boundary)
		
	assert input_tuple
	array = list(input_tuple)
	sort_part(array, 0, len(array) - 1);
	return tuple(array)


if __name__ == '__main__':
	pass
