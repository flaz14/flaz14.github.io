# -*- coding: utf-8 -*-


def sort_procedural(input_tuple):
	def sort_part(array, left_boundary, right_boundary):
		def get_pivot(array, left_boundary, right_boundary):
			return array[left_boundary + (right_boundary - left_boundary) // 2]
		
		def swap(array, left, right):
			array[left], array[right] = array[right], array[left]
		
		pivot = get_pivot(array, left_boundary, right_boundary)
		left = left_boundary
		right = right_boundary
		while left < right:
			while array[left] < pivot:
				left += 1
			while array[right] > pivot:
				right -= 1
			if left <= right:
				swap(array, left, right)
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


########################################################################################################################


def sort(input_tuple):
	def sort_part(array, left_boundary, right_boundary):
		def get_pivot(array, left_boundary, right_boundary):
			return array[left_boundary + (right_boundary - left_boundary) // 2]
		
		def swap(array, left, right):
			array[left], array[right] = array[right], array[left]
		
		def get_left(array, left, pivot):
			while array[left] < pivot:
				left += 1
			return left
		
		def get_right(array, right, pivot):
			while array[right] > pivot:
				right -= 1
			return right
		
		pivot = get_pivot(array, left_boundary, right_boundary)
		left = left_boundary
		right = right_boundary
		while left < right:
			left = get_left(array, left, pivot)
			right = get_right(array, right, pivot)
			if left <= right:
				swap(array, left, right)
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
