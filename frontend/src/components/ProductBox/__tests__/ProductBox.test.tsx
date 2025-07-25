import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import ProductBox from '../index'

// Mock the utils functions
vi.mock('../../../utils', () => ({
  getProductById: vi.fn(),
  handleProductInOrderList: vi.fn(),
}))

const mockProps = {
  productId: '1',
  tableActive: 'table1',
  orderList: [],
  setOrderList: vi.fn(),
}

describe('ProductBox', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders without crashing', () => {
    render(<ProductBox {...mockProps} />)
    expect(screen.getByTestId('product-box')).toBeInTheDocument()
  })

  it('displays product information when loaded', async () => {
    const mockProduct = {
      id: '1',
      name: 'Test Product',
      price: 10.99,
      image: 'test-image.jpg'
    }
    
    const { getProductById } = await import('../../../utils')
    vi.mocked(getProductById).mockResolvedValue(mockProduct)

    render(<ProductBox {...mockProps} />)
    
    // Wait for product to load and check if name is displayed
    expect(await screen.findByText('Test Product')).toBeInTheDocument()
  })
})