import { useState } from 'react';
import { registerCustomer, ApiError } from '../../utils/api';
import './index.css';

function CustomerRegistration({ stateUpdater }) {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    username: '',
    password: '',
    phone: '',
    address: '',
    birthDate: '',
    gender: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess(false);

    try {
      await registerCustomer(formData);
      
      setSuccess(true);
      setFormData({
        firstName: '',
        lastName: '',
        email: '',
        username: '',
        password: '',
        phone: '',
        address: '',
        birthDate: '',
        gender: '',
      });
      stateUpdater(false);
    } catch (err) {
      if (err instanceof ApiError) {
        setError(err.message);
      } else {
        setError('Network error. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className='customer-registration-container'>
      <div className='registration-form'>
        <h2>Customer Registration</h2>

        {success && (
          <div className='success-message'>
            Customer registered successfully!
          </div>
        )}

        {error && <div className='error-message'>{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className='form-row'>
            <div className='form-group'>
              <label htmlFor='firstName'>First Name *</label>
              <input
                type='text'
                id='firstName'
                name='firstName'
                value={formData.firstName}
                onChange={handleInputChange}
                required
                placeholder='Enter first name'
              />
            </div>

            <div className='form-group'>
              <label htmlFor='lastName'>Last Name *</label>
              <input
                type='text'
                id='lastName'
                name='lastName'
                value={formData.lastName}
                onChange={handleInputChange}
                required
                placeholder='Enter last name'
              />
            </div>
          </div>

          <div className='form-row'>
            <div className='form-group'>
              <label htmlFor='email'>Email *</label>
              <input
                type='email'
                id='email'
                name='email'
                value={formData.email}
                onChange={handleInputChange}
                required
                placeholder='Enter email address'
              />
            </div>

            <div className='form-group'>
              <label htmlFor='username'>Username *</label>
              <input
                type='text'
                id='username'
                name='username'
                value={formData.username}
                onChange={handleInputChange}
                required
                placeholder='Enter username'
              />
            </div>
          </div>

          <div className='form-group'>
            <label htmlFor='password'>Password *</label>
            <input
              type='password'
              id='password'
              name='password'
              value={formData.password}
              onChange={handleInputChange}
              required
              placeholder='Enter password'
            />
          </div>

          <div className='form-row'>
            <div className='form-group'>
              <label htmlFor='phone'>Phone</label>
              <input
                type='tel'
                id='phone'
                name='phone'
                value={formData.phone}
                onChange={handleInputChange}
                placeholder='Enter phone number'
              />
            </div>

            <div className='form-group'>
              <label htmlFor='birthDate'>Birth Date</label>
              <input
                type='date'
                id='birthDate'
                name='birthDate'
                value={formData.birthDate}
                onChange={handleInputChange}
              />
            </div>
          </div>

          <div className='form-row'>
            <div className='form-group'>
              <label htmlFor='gender'>Gender</label>
              <select
                id='gender'
                name='gender'
                value={formData.gender}
                onChange={handleInputChange}
              >
                <option value=''>Select gender</option>
                <option value='Male'>Male</option>
                <option value='Female'>Female</option>
                <option value='Other'>Other</option>
              </select>
            </div>
          </div>

          <div className='form-group'>
            <label htmlFor='address'>Address</label>
            <textarea
              id='address'
              name='address'
              value={formData.address}
              onChange={handleInputChange}
              placeholder='Enter full address'
              rows='3'
            />
          </div>

          <button type='submit' disabled={loading} className='submit-button'>
            {loading ? 'Registering...' : 'Register Customer'}
          </button>
        </form>
      </div>
    </div>
  );
}

export { CustomerRegistration };
