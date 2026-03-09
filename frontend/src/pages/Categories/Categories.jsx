import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { categoryService } from '../../api/services';

export default function Categories() {
  const { user } = useAuth();
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingCategory, setEditingCategory] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    type: 'EXPENSE',
    color: '#6B7280',
    icon: 'default',
    description: '',
  });

  const iconOptions = [
    { value: 'food', label: '🍔 Alimentação', emoji: '🍔' },
    { value: 'money', label: '💰 Salário', emoji: '💰' },
    { value: 'home', label: '🏠 Casa', emoji: '🏠' },
    { value: 'car', label: '🚗 Transporte', emoji: '🚗' },
    { value: 'health', label: '🏥 Saúde', emoji: '🏥' },
    { value: 'education', label: '📚 Educação', emoji: '📚' },
    { value: 'entertainment', label: '🎮 Lazer', emoji: '🎮' },
    { value: 'shopping', label: '🛒 Compras', emoji: '🛒' },
    { value: 'default', label: '📋 Outros', emoji: '📋' },
  ];

  const colorOptions = [
    '#EF4444', // Vermelho
    '#F59E0B', // Laranja
    '#10B981', // Verde
    '#3B82F6', // Azul
    '#8B5CF6', // Roxo
    '#EC4899', // Rosa
    '#6B7280', // Cinza
    '#14B8A6', // Teal
  ];

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      setLoading(true);
      const response = await categoryService.getAll(user.userId);
      setCategories(response.data);
    } catch (error) {
      console.error('Erro ao carregar categorias:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingCategory) {
        await categoryService.update(user.userId, editingCategory.id, formData);
      } else {
        await categoryService.create(user.userId, formData);
      }
      loadCategories();
      handleCloseModal();
    } catch (error) {
      console.error('Erro ao salvar categoria:', error);
      alert(error.response?.data?.message || 'Erro ao salvar categoria');
    }
  };

  const handleEdit = (category) => {
    setEditingCategory(category);
    setFormData({
      name: category.name,
      type: category.type,
      color: category.color,
      icon: category.icon,
      description: category.description || '',
    });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir esta categoria?')) {
      try {
        await categoryService.delete(user.userId, id);
        loadCategories();
      } catch (error) {
        console.error('Erro ao deletar categoria:', error);
        alert('Não é possível excluir categorias que possuem transações vinculadas');
      }
    }
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setEditingCategory(null);
    setFormData({
      name: '',
      type: 'EXPENSE',
      color: '#6B7280',
      icon: 'default',
      description: '',
    });
  };

  const getIconEmoji = (iconValue) => {
    const icon = iconOptions.find((i) => i.value === iconValue);
    return icon ? icon.emoji : '📋';
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-xl text-gray-600">Carregando...</div>
      </div>
    );
  }

  const incomeCategories = categories.filter((cat) => cat.type === 'INCOME');
  const expenseCategories = categories.filter((cat) => cat.type === 'EXPENSE');

  return (
    <div>
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-800">Categorias</h1>
          <p className="text-gray-600 mt-1">Organize suas transações por categorias</p>
        </div>
        <button
          onClick={() => setShowModal(true)}
          className="bg-blue-600 hover:bg-blue-700 text-white font-semibold px-6 py-3 rounded-lg transition"
        >
          + Nova Categoria
        </button>
      </div>

      {/* Categorias de Receita */}
      <div className="mb-8">
        <h2 className="text-xl font-bold text-gray-800 mb-4">Receitas</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {incomeCategories.length > 0 ? (
            incomeCategories.map((category) => (
              <div
                key={category.id}
                className="bg-white rounded-xl shadow-md p-6 hover:shadow-lg transition"
              >
                <div className="flex items-start justify-between mb-4">
                  <div className="flex items-center">
                    <div
                      className="w-12 h-12 rounded-full flex items-center justify-center text-2xl mr-3"
                      style={{ backgroundColor: category.color + '20' }}
                    >
                      {getIconEmoji(category.icon)}
                    </div>
                    <div>
                      <h3 className="font-bold text-gray-800">{category.name}</h3>
                      <p className="text-sm text-gray-500">
                        {category.description || 'Sem descrição'}
                      </p>
                    </div>
                  </div>
                </div>
                <div className="flex space-x-2">
                  <button
                    onClick={() => handleEdit(category)}
                    className="flex-1 bg-blue-50 hover:bg-blue-100 text-blue-600 font-medium py-2 rounded-lg transition"
                  >
                    Editar
                  </button>
                  <button
                    onClick={() => handleDelete(category.id)}
                    className="flex-1 bg-red-50 hover:bg-red-100 text-red-600 font-medium py-2 rounded-lg transition"
                  >
                    Excluir
                  </button>
                </div>
              </div>
            ))
          ) : (
            <div className="col-span-full text-center py-8 text-gray-500">
              Nenhuma categoria de receita cadastrada
            </div>
          )}
        </div>
      </div>

      {/* Categorias de Despesa */}
      <div>
        <h2 className="text-xl font-bold text-gray-800 mb-4">Despesas</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {expenseCategories.length > 0 ? (
            expenseCategories.map((category) => (
              <div
                key={category.id}
                className="bg-white rounded-xl shadow-md p-6 hover:shadow-lg transition"
              >
                <div className="flex items-start justify-between mb-4">
                  <div className="flex items-center">
                    <div
                      className="w-12 h-12 rounded-full flex items-center justify-center text-2xl mr-3"
                      style={{ backgroundColor: category.color + '20' }}
                    >
                      {getIconEmoji(category.icon)}
                    </div>
                    <div>
                      <h3 className="font-bold text-gray-800">{category.name}</h3>
                      <p className="text-sm text-gray-500">
                        {category.description || 'Sem descrição'}
                      </p>
                    </div>
                  </div>
                </div>
                <div className="flex space-x-2">
                  <button
                    onClick={() => handleEdit(category)}
                    className="flex-1 bg-blue-50 hover:bg-blue-100 text-blue-600 font-medium py-2 rounded-lg transition"
                  >
                    Editar
                  </button>
                  <button
                    onClick={() => handleDelete(category.id)}
                    className="flex-1 bg-red-50 hover:bg-red-100 text-red-600 font-medium py-2 rounded-lg transition"
                  >
                    Excluir
                  </button>
                </div>
              </div>
            ))
          ) : (
            <div className="col-span-full text-center py-8 text-gray-500">
              Nenhuma categoria de despesa cadastrada
            </div>
          )}
        </div>
      </div>

      {/* Modal de Criação/Edição */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl shadow-2xl w-full max-w-md p-6">
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-2xl font-bold text-gray-800">
                {editingCategory ? 'Editar Categoria' : 'Nova Categoria'}
              </h2>
              <button
                onClick={handleCloseModal}
                className="text-gray-400 hover:text-gray-600"
              >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Tipo
                </label>
                <select
                  value={formData.type}
                  onChange={(e) => setFormData({ ...formData, type: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  required
                >
                  <option value="EXPENSE">Despesa</option>
                  <option value="INCOME">Receita</option>
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Nome
                </label>
                <input
                  type="text"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  required
                  minLength={2}
                  maxLength={50}
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Ícone
                </label>
                <select
                  value={formData.icon}
                  onChange={(e) => setFormData({ ...formData, icon: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                >
                  {iconOptions.map((icon) => (
                    <option key={icon.value} value={icon.value}>
                      {icon.label}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Cor
                </label>
                <div className="grid grid-cols-4 gap-2">
                  {colorOptions.map((color) => (
                    <button
                      key={color}
                      type="button"
                      onClick={() => setFormData({ ...formData, color })}
                      className={`w-full h-12 rounded-lg transition ${
                        formData.color === color
                          ? 'ring-4 ring-blue-500 ring-offset-2'
                          : 'hover:scale-110'
                      }`}
                      style={{ backgroundColor: color }}
                    />
                  ))}
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Descrição (opcional)
                </label>
                <textarea
                  value={formData.description}
                  onChange={(e) =>
                    setFormData({ ...formData, description: e.target.value })
                  }
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  rows={3}
                  maxLength={200}
                />
              </div>

              <div className="flex space-x-3 pt-4">
                <button
                  type="button"
                  onClick={handleCloseModal}
                  className="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold py-2 rounded-lg transition"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 rounded-lg transition"
                >
                  {editingCategory ? 'Atualizar' : 'Criar'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}