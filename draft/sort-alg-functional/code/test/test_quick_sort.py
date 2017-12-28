# -*- coding: utf-8 -*-


import os, imp
imp.load_source('quick_sort', os.path.join(os.path.dirname(__file__), '../quick_sort.py'))
import pytest
import quick_sort


def test_empty_array():
	with pytest.raises(AssertionError):
		quick_sort.sort(())


def test_none():
	with pytest.raises(AssertionError):
		quick_sort.sort(None)


def test_one_item_array():
	sorted_array = quick_sort.sort((2,))
	assert (2,) == sorted_array


def test_two_items_array():
	sorted_array = quick_sort.sort((5, 3))
	assert (3, 5) == sorted_array	